package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.inventory.InventoryType
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.sale.TransactionType
import com.zaed.common.data.model.sale.WholesaleGoldSale
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.data.model.sale.WholesaleSale
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleProductSaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleGoldSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleProductSaleRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class SaleRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : SaleRemoteSource {
    private val storeSalesCollection = firestore.collection("store_sales")
    private val wholesaleProductSalesCollection = firestore.collection("wholesale_product_sales")
    private val wholesaleGoldSalesCollection = firestore.collection("wholesale_gold_sales")
    private val ingotTransactionsCollection = firestore.collection("ingot_transactions")
    private val moneyPaymentCollection = firestore.collection("money_payments")
    private val wholesaleCustomersCollection = firestore.collection("whole_sale_customers")
    private val inventoryCollection = firestore.collection("inventory")
    private val distributorLossesCollection = firestore.collection("distributor-losses")

    override fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>> =
        callbackFlow {
            try {
                storeSalesCollection.where(
                    Filter.and(
                        Filter.equalTo("storeId", request.storeId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        crashlytics.recordException(e)
                        trySend(Result.failure(e))
                        return@addSnapshotListener
                    }
                    val storeSales = snapshot?.toObjects(StoreSale::class.java) ?: emptyList()
                    trySend(Result.success(storeSales))
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            } finally {
                awaitClose { }
            }
        }

    override suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val docRef = storeSalesCollection.document()
            val receiptNumber = storeSalesCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            batch.set(
                docRef,
                request.sale.copy(id = docRef.id, receiptNumber = (receiptNumber + 1).toString())
            )
            val categoryUpdates = request.sale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val inventoryQuery = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", categoryUpdates.keys.toList()),
                        Filter.equalTo("ownerId", request.sale.storeId)
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryQuery.documents.associateBy {
                it.getString("productId") ?: ""
            }
            categoryUpdates.forEach { (categoryId, totalGrams) ->
                val inventoryDoc = inventoryByProductId[categoryId]
                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(totalGrams.unaryMinus()))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            batch.commit().await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleRef = storeSalesCollection.document(request.saleId)
            val sale = saleRef.get().await()
                .toObject(StoreSale::class.java) ?: StoreSale()
            val logs = sale.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.employeeId,
                    employeeName = request.employeeName,
                    type = LogType.DELETE
                )
            )
            val inventoryChanges = sale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val inventoryRefs = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", inventoryChanges.keys.toList()),
                        Filter.equalTo("ownerId", sale.storeId)
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryRefs.documents.associateBy {
                it.getString("productId") ?: ""
            }
            inventoryChanges.forEach { (categoryId, netChange) ->
                val inventoryDoc = inventoryByProductId[categoryId]

                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(netChange))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            batch.set(saleRef, sale.copy(logs = logs, deleted = true))
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val oldSaleRef = storeSalesCollection.document(request.sale.id)
            val oldSale = oldSaleRef.get().await()
                .toObject(StoreSale::class.java) ?: StoreSale()
            val logs = oldSale.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.employeeId,
                    employeeName = request.employeeName,
                    type = LogType.UPDATE
                )
            )
            if (isProductsDifferent(oldSale, request.sale)) {
                logs.add(
                    ChangeLog(
                        date = Date(),
                        employeeId = request.employeeId,
                        employeeName = request.employeeName,
                        type = LogType.UPDATE
                    )
                )
                val oldProductsByCategory = oldSale.products
                    .groupBy { it.categoryId }
                    .mapValues { (_, products) -> products.sumOf { it.grams } }
                val newProductsByCategory = request.sale.products
                    .groupBy { it.categoryId }
                    .mapValues { (_, products) -> products.sumOf { it.grams } }
                val storeId = request.sale.storeId
                val inventoryChanges =
                    (oldProductsByCategory.keys + newProductsByCategory.keys).associateWith { categoryId ->
                        val oldAmount = oldProductsByCategory[categoryId] ?: 0.0
                        val newAmount = newProductsByCategory[categoryId] ?: 0.0
                        oldAmount - newAmount
                    }
                val inventoryRefs = inventoryCollection
                    .where(
                        Filter.and(
                            Filter.inArray("productId", inventoryChanges.keys.toList()),
                            Filter.equalTo("ownerId", request.sale.storeId)
                        )
                    )
                    .get().await()

                val inventoryByProductId = inventoryRefs.documents.associateBy {
                    it.getString("productId") ?: ""
                }
                inventoryChanges.forEach { (categoryId, netChange) ->
                    val inventoryDoc = inventoryByProductId[categoryId]

                    if (inventoryDoc != null) {
                        val updates = mapOf("quantity" to FieldValue.increment(netChange))
                        batch.update(inventoryDoc.reference, updates)
                    }
                }
            }
            batch.set(oldSaleRef, request.sale.copy(logs = logs), SetOptions.merge())
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun getStoreSale(saleId: String): Result<StoreSale> {
        return try {
            val storeSale =
                storeSalesCollection.document(saleId).get().await().toObject(StoreSale::class.java)
            if (storeSale != null) {
                Result.success(storeSale)
            } else {
                Result.failure(Exception("Store sale not found"))
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleSale>>> =
        callbackFlow {
            var goldSalesListener: ListenerRegistration? = null
            var productSalesListener: ListenerRegistration? = null
            try {
                var latestGoldSales: List<WholesaleGoldSale> = emptyList()
                var latestProductSales: List<WholesaleProductSale> = emptyList()
                val updateAndSend = {
                    val combinedSales = (latestGoldSales + latestProductSales)
                        .sortedByDescending { it.createdAt } // Sort by date
                    trySend(Result.success(combinedSales))
                }

                goldSalesListener = wholesaleGoldSalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", request.distributorId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    latestGoldSales = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(WholesaleGoldSale::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    updateAndSend()
                }

                productSalesListener = wholesaleProductSalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", request.distributorId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    latestProductSales = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(WholesaleProductSale::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    updateAndSend()
                }

            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {
                goldSalesListener?.remove()
                productSalesListener?.remove()
            }
        }

    override fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleSale>>> =
        callbackFlow {
            var goldSalesListener: ListenerRegistration? = null
            var productSalesListener: ListenerRegistration? = null
            try {
                var latestGoldSales: List<WholesaleGoldSale> = emptyList()
                var latestProductSales: List<WholesaleProductSale> = emptyList()
                val updateAndSend = {
                    val combinedSales = (latestGoldSales + latestProductSales)
                        .sortedByDescending { it.createdAt } // Sort by date
                    trySend(Result.success(combinedSales))
                }

                goldSalesListener = wholesaleGoldSalesCollection.where(
                    Filter.and(
                        Filter.equalTo("customerId", request.customerId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    latestGoldSales = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(WholesaleGoldSale::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    updateAndSend()
                }

                productSalesListener = wholesaleProductSalesCollection.where(
                    Filter.and(
                        Filter.equalTo("customerId", request.customerId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    latestProductSales = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(WholesaleProductSale::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    updateAndSend()
                }


            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {
                goldSalesListener?.remove()
                productSalesListener?.remove()
            }
        }

    override fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>> =
        callbackFlow {
            try {
                ingotTransactionsCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", request.distributorId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        crashlytics.recordException(e)
                        trySend(Result.failure(e))
                        return@addSnapshotListener
                    }
                    val transactions =
                        snapshot?.toObjects(IngotTransaction::class.java) ?: emptyList()
                    trySend(Result.success(transactions))
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun addIngotTransaction(request: AddIngotTransactionRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val docRef = ingotTransactionsCollection.document()
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.INGOT),
                        Filter.equalTo("karat", request.transaction.karat),
                        Filter.equalTo("ownerId", request.transaction.distributorId)
                    )
                ).get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val updates =
                        mapOf(
                            "quantity" to FieldValue.increment(
                                if (request.transaction.type == TransactionType.SALE)
                                    request.transaction.grams.unaryMinus()
                                else
                                    request.transaction.grams
                            )
                        )
                    batch.update(ref, updates)
                }
            batch.set(docRef, request.transaction.copy(id = docRef.id))
            batch.commit().await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateIngotTransaction(request: UpdateIngotTransactionRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val oldTransactionRef = ingotTransactionsCollection.document(request.transaction.id)
            val oldTransaction =
                oldTransactionRef.get().await().toObject(IngotTransaction::class.java)
                    ?: IngotTransaction()
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.INGOT),
                        Filter.equalTo("karat", request.transaction.karat),
                        Filter.equalTo("ownerId", request.transaction.distributorId)
                    )
                ).get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val oldAmount =
                        if (oldTransaction.type == TransactionType.SALE) oldTransaction.grams.unaryMinus() else oldTransaction.grams
                    val newAmount =
                        if (request.transaction.type == TransactionType.SALE) request.transaction.grams.unaryMinus() else request.transaction.grams
                    val updates = mapOf(
                        "quantity" to FieldValue.increment(
                            if (request.transaction.deleted) oldAmount else newAmount - oldAmount
                        )
                    )
                    batch.update(ref, updates)
                }
            batch.set(oldTransactionRef, request.transaction)
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun fetchAllStoreSales(): Flow<Result<List<StoreSale>>> =
        callbackFlow {
            try {
                storeSalesCollection
                    .orderBy(
                        "createdAt",
                        Query.Direction.DESCENDING
                    ).addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            crashlytics.recordException(e)
                            trySend(Result.failure(e))
                            return@addSnapshotListener
                        }
                        val storeSales = snapshot?.toObjects(StoreSale::class.java) ?: emptyList()
                        trySend(Result.success(storeSales))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleSale>>> = callbackFlow {
        var goldSalesListener: ListenerRegistration? = null
        var productSalesListener: ListenerRegistration? = null
        try {
            var latestGoldSales: List<WholesaleGoldSale> = emptyList()
            var latestProductSales: List<WholesaleProductSale> = emptyList()
            val updateAndSend = {
                val combinedSales = (latestGoldSales + latestProductSales)
                    .sortedByDescending { it.createdAt } // Sort by date
                trySend(Result.success(combinedSales))
            }

            goldSalesListener = wholesaleGoldSalesCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                latestGoldSales = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(WholesaleGoldSale::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                updateAndSend()
            }

            productSalesListener = wholesaleProductSalesCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                latestProductSales = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(WholesaleProductSale::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                updateAndSend()
            }

        } catch (e: Exception) {
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose {
            goldSalesListener?.remove()
            productSalesListener?.remove()
        }
    }


    override suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleRef = wholesaleProductSalesCollection.document(request.saleId)
            val sale = saleRef.get().await()
                .toObject(WholesaleProductSale::class.java) ?: WholesaleProductSale()
            val logs = sale.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    type = LogType.DELETE
                )
            )
            batch.set(saleRef, sale.copy(logs = logs, deleted = true))
            val customerRef = wholesaleCustomersCollection.document(sale.customerId)
            var totalPaymentDeleted = 0.0
            sale.paymentsIds.forEach {
                val paymentRef = moneyPaymentCollection.document(it)
                val payment = paymentRef.get().await().toObject(CashPayment::class.java)
                val log = ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    type = LogType.DELETE
                )
                val updatePayments = mapOf(
                    "deleted" to true,
                    "logs" to FieldValue.arrayUnion(log)
                )
                batch.update(paymentRef, updatePayments)
                if (payment?.type == PaymentType.FUTURES) {
                    totalPaymentDeleted += payment.amount
                }
            }
            val inventoryChanges = sale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val distributorId = sale.distributorId
            val inventoryRefs = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", inventoryChanges.keys.toList()),
                        Filter.equalTo("ownerId", distributorId)
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryRefs.documents.associateBy {
                it.getString("productId") ?: ""
            }
            inventoryChanges.forEach { (categoryId, netChange) ->
                val inventoryDoc = inventoryByProductId[categoryId]
                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(netChange))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            batch.update(
                customerRef,
                mapOf("debtAmount" to FieldValue.increment(totalPaymentDeleted.unaryMinus()))
            )
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleGoldSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleRef = wholesaleGoldSalesCollection.document(request.saleId)
            val sale = saleRef.get().await()
                .toObject(WholesaleGoldSale::class.java) ?: WholesaleGoldSale()
            val logs = sale.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    type = LogType.DELETE
                )
            )
            batch.set(saleRef, sale.copy(logs = logs, deleted = true))
            val customerRef = wholesaleCustomersCollection.document(sale.customerId)
            var totalPaymentDeleted = 0.0
            sale.paymentsIds.forEach {
                val paymentRef = moneyPaymentCollection.document(it)
                val payment = paymentRef.get().await().toObject(CashPayment::class.java)
                val log = ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    type = LogType.DELETE
                )
                val updatePayments = mapOf(
                    "deleted" to true,
                    "logs" to FieldValue.arrayUnion(log)
                )
                batch.update(paymentRef, updatePayments)
                if (payment?.type == PaymentType.FUTURES) {
                    totalPaymentDeleted += payment.amount
                }
            }
            batch.update(
                customerRef,
                mapOf("debtAmount" to FieldValue.increment(totalPaymentDeleted.unaryMinus()))
            )
            val goldAmount = sale.products.sumOf { it.grams }
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.GOLD),
                        Filter.equalTo("ownerId", sale.distributorId)
                    )
                )
                .get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val updates = mapOf("quantity" to FieldValue.increment(goldAmount))
                    batch.update(ref, updates)
                }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun fetchWholesaleProductSale(request: FetchWholesaleProductSaleRequest): Result<WholesaleProductSale> {
        return try {
            val sale = wholesaleProductSalesCollection.document(request.saleId).get().await()
                .toObject(WholesaleProductSale::class.java) ?: WholesaleProductSale()
            Result.success(sale)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun fetchWholesaleGoldSale(request: FetchWholesaleGoldSaleRequest): Result<WholesaleGoldSale> {
        return try {
            val sale = wholesaleGoldSalesCollection.document(request.saleId).get().await()
                .toObject(WholesaleGoldSale::class.java) ?: WholesaleGoldSale()
            Result.success(sale)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun addWholesaleProductSale(request: AddWholesaleProductSaleRequest): Result<String> {
        return try {
            Log.d("add_sale", "invoke remote: $request")
            val batch = firestore.batch()
            val docRef = wholesaleProductSalesCollection.document()
            val paymentsIds = mutableListOf<String>()
            val maxProductReceipt = wholesaleProductSalesCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val maxGoldReceiptNumber = wholesaleGoldSalesCollection.orderBy(
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val receiptNumber = maxOf(maxProductReceipt, maxGoldReceiptNumber) + 1

            request.payments.forEach {
                val ref = moneyPaymentCollection.document()
                paymentsIds.add(ref.id)

                if (it.type == PaymentType.FUTURES) {
                    val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
                    batch.update(
                        customerRef,
                        mapOf("debtAmount" to FieldValue.increment(it.amount.unaryMinus()))
                    )
                } else if (it.type == PaymentType.LOSS) {
                    val document = distributorLossesCollection.document()
                    batch.set(
                        document, DistributorLoss(
                            id = document.id,
                            value = it.amount,
                            reason = "Sales Loss for $receiptNumber",
                            userId = request.sale.distributorId,
                            userName = request.sale.distributorName,
                            logs = listOf(
                                ChangeLog(
                                    employeeId = request.sale.distributorId,
                                    employeeName = request.sale.distributorName,
                                    type = LogType.CREATE
                                )
                            )
                        )
                    )
                } else {
                }
                when (it) {
                    is CashPayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )

                    is FuturePayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )

                    is ChequePayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )

                    is BankTransferPayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )
                }
            }
            Log.d("add_sale", "invoke remote2: $request")
            val categoryUpdates = request.sale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val inventoryQuery = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", categoryUpdates.keys.toList()),
                        Filter.equalTo("ownerId", request.sale.distributorId)
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryQuery.documents.associateBy {
                it.getString("productId") ?: ""
            }
            categoryUpdates.forEach { (categoryId, totalGrams) ->
                val inventoryDoc = inventoryByProductId[categoryId]
                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(totalGrams.unaryMinus()))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            Log.d("add_sale", "invoke remote3: $receiptNumber, $paymentsIds")
            batch.set(
                docRef,
                request.sale.copy(
                    id = docRef.id,
                    paymentsIds = paymentsIds,
                    receiptNumber = receiptNumber.toString()
                )
            )
            batch.commit().await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun addGoldSale(request: AddWholesaleGoldSaleRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val docRef = wholesaleGoldSalesCollection.document()
            val moneyPaymentsIds = mutableListOf<String>()
            val maxProductReceipt = wholesaleProductSalesCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val maxGoldReceiptNumber = wholesaleGoldSalesCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            var receiptNumber = maxOf(maxProductReceipt, maxGoldReceiptNumber) + 1
            request.payments.forEach {
                val ref = moneyPaymentCollection.document()
                moneyPaymentsIds.add(ref.id)
                var amount = if (it.type == PaymentType.FUTURES) {
                    val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
                    batch.update(
                        customerRef,
                        mapOf("debtAmount" to FieldValue.increment(it.amount.unaryMinus()))
                    )
                    it.amount.unaryMinus()
                } else {
                    it.amount
                }
                batch.set(
                    ref,
                    it.apply {
                        this.id = ref.id
                        this.receiptNumber = receiptNumber.toString()
                        this.amount = amount
                    }
                )
            }
            val goldAmount = request.sale.products.sumOf { it.grams }
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.GOLD),
                        Filter.equalTo("ownerId", request.sale.distributorId)
                    )
                )
                .get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val updates = mapOf("quantity" to FieldValue.increment(goldAmount.unaryMinus()))
                    batch.update(ref, updates)
                }
            batch.set(
                docRef,
                request.sale.copy(
                    id = docRef.id,
                    paymentsIds = moneyPaymentsIds,
                    receiptNumber = receiptNumber.toString()
                )
            )
            batch.commit().await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }


    override suspend fun updateWholesaleProductSale(request: UpdateWholesaleProductSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleDocRef = wholesaleProductSalesCollection.document(request.sale.id)
            val existingSale =
                wholesaleProductSalesCollection.document(request.sale.id).get().await()
                    .toObject(WholesaleProductSale::class.java)
                    ?: return Result.failure(Exception("Sale not found"))
            val existingPaymentIds = existingSale.paymentsIds
            val existingPayment = moneyPaymentCollection.where(
                Filter.and(
                    Filter.inArray("id", existingPaymentIds),
                    Filter.equalTo("type", PaymentType.FUTURES)
                )
            ).get().await().documents.firstOrNull()?.toObject(CashPayment::class.java)
                ?: CashPayment()
            val updatedPaymentIds = mutableListOf<String>()
            if (request.sale.customerId.isNotBlank()) {
                val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)

                Log.d("finding_the_sex", "existingPayment: ${existingPayment.amount}")
                batch.update(
                    customerRef,
                    mapOf("debtAmount" to FieldValue.increment(existingPayment.signedAmount().unaryMinus()))
                )
            }
            request.payments.forEach { payment ->

                if (payment.id.isNotEmpty() && existingPaymentIds.contains(payment.id)) {
                    val paymentRef = moneyPaymentCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = moneyPaymentCollection.document()
                    val amount = if (payment.type == PaymentType.FUTURES) {
                        val customerRef =
                            wholesaleCustomersCollection.document(request.sale.customerId)
                        batch.update(
                            customerRef,
                            mapOf("debtAmount" to FieldValue.increment(payment.signedAmount()))
                        )
                        payment.amount
                    } else {
                        payment.amount
                    }
                    when (payment) {
                        is CashPayment -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )

                        is FuturePayment -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )

                        is ChequePayment -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )

                        is BankTransferPayment -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )

                        is LossPayment -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )
                    }
                    updatedPaymentIds.add(newPaymentRef.id)
                }

            }
            existingPaymentIds.forEach { paymentId ->
                if (!updatedPaymentIds.contains(paymentId)) {
                    batch.delete(moneyPaymentCollection.document(paymentId))
                }
            }
            val oldProductsByCategory = existingSale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val newProductsByCategory = request.sale.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val distributorId = request.sale.distributorId
            val inventoryChanges =
                (oldProductsByCategory.keys + newProductsByCategory.keys).associateWith { categoryId ->
                    val oldAmount = oldProductsByCategory[categoryId] ?: 0.0
                    val newAmount = newProductsByCategory[categoryId] ?: 0.0
                    oldAmount - newAmount
                }
            val inventoryRefs = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", inventoryChanges.keys.toList()),
                        Filter.equalTo("ownerId", distributorId)
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryRefs.documents.associateBy {
                it.getString("productId") ?: ""
            }
            inventoryChanges.forEach { (categoryId, netChange) ->
                val inventoryDoc = inventoryByProductId[categoryId]

                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(netChange))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            batch.set(
                saleDocRef, request.sale.copy(paymentsIds = updatedPaymentIds),
                SetOptions.merge()
            )
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateWholesaleGoldSale(request: UpdateWholesaleGoldSaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleDocRef = wholesaleGoldSalesCollection.document(request.sale.id)
            val existingSale =
                wholesaleGoldSalesCollection.document(request.sale.id).get().await()
                    .toObject(WholesaleGoldSale::class.java)
                    ?: return Result.failure(Exception("Sale not found"))
            val existingMoneyPaymentIds = existingSale.paymentsIds
            val existingPayment = moneyPaymentCollection.where(
                Filter.and(
                    Filter.inArray("id", existingMoneyPaymentIds),
                    Filter.equalTo("type", PaymentType.FUTURES)
                )
            ).get().await().documents.firstOrNull()?.toObject(CashPayment::class.java)
                ?: CashPayment()
            val updatedMoneyPaymentIds = mutableListOf<String>()
            if (request.sale.customerId.isNotBlank()) {
                val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
                batch.update(
                    customerRef,
                    mapOf("debtAmount" to FieldValue.increment(existingPayment.signedAmount().unaryMinus()))
                )
            }
            request.payments.forEach { payment ->
                if (payment.id.isNotEmpty() && existingMoneyPaymentIds.contains(payment.id)) {
                    val paymentRef = moneyPaymentCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedMoneyPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = moneyPaymentCollection.document()
                    val amount = if (payment.type == PaymentType.FUTURES) {
                        val customerRef =
                            wholesaleCustomersCollection.document(request.sale.customerId)
                        batch.update(
                            customerRef,
                            mapOf("debtAmount" to FieldValue.increment(payment.signedAmount()))
                        )
                        payment.amount
                    } else {
                        payment.amount
                    }
                    batch.set(
                        newPaymentRef,
                        payment.apply {
                            this.id = newPaymentRef.id
                            this.amount = amount
                        }
                    )
                    updatedMoneyPaymentIds.add(newPaymentRef.id)
                }
            }
            existingMoneyPaymentIds.forEach { paymentId ->
                if (!updatedMoneyPaymentIds.contains(paymentId)) {
                    batch.delete(moneyPaymentCollection.document(paymentId))
                }
            }
            batch.set(
                saleDocRef, request.sale.copy(paymentsIds = updatedMoneyPaymentIds),
                SetOptions.merge()
            )
            //

            batch.set(
                saleDocRef, request.sale.copy(
                    paymentsIds = updatedMoneyPaymentIds
                ),
                SetOptions.merge()
            )
            val oldGoldAmount = existingSale.products.sumOf { it.grams }
            val newGoldAmount = request.sale.products.sumOf { it.grams }
            val goldUpdate = oldGoldAmount - newGoldAmount
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.GOLD),
                        Filter.equalTo("ownerId", request.sale.distributorId)
                    )
                )
                .get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val updates = mapOf("quantity" to FieldValue.increment(goldUpdate))
                    batch.update(ref, updates)
                }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    private companion object {

        fun isProductsDifferent(sale1: StoreSale, sale2: StoreSale): Boolean {
            return sale1.products != sale2.products
        }
    }
}