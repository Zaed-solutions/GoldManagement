package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.zaed.common.data.model.inventory.InventoryType
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentType
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
    private val categoriesCollection = firestore.collection("categories")
    private val wholesaleProductSalesCollection = firestore.collection("wholesale_product_sales")
    private val wholesaleGoldSalesCollection = firestore.collection("wholesale_gold_sales")
    private val paymentsCollection = firestore.collection("payments")
    private val ingotTransactionsCollection = firestore.collection("ingot_transactions")
    private val moneyPaymentCollection = firestore.collection("money_payments")
    private val goldPaymentsCollection = firestore.collection("gold_payments")
    private val wholesaleCustomersCollection = firestore.collection("whole_sale_customers")
    private val inventoryCollection = firestore.collection("inventory")
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
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            batch.set(docRef, request.sale.copy(id = docRef.id, receiptNumber = (receiptNumber + 1).toString()))
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
                    action = "${request.employeeName} Deleted this sale"
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
            if (isCustomerDifferent(oldSale, request.sale)) {
                logs.add(
                    ChangeLog(
                        date = Date(),
                        employeeId = request.employeeId,
                        employeeName = request.employeeName,
                        action = "${request.employeeName} Changed the customer from ${oldSale.customerName}-${oldSale.customerEmail}-${oldSale.customerPhone} to ${request.sale.customerName}-${request.sale.customerEmail}-${request.sale.customerPhone}"
                    )
                )
            }
            if (isProductsDifferent(oldSale, request.sale)) {
                logs.add(
                    ChangeLog(
                        date = Date(),
                        employeeId = request.employeeId,
                        employeeName = request.employeeName,
                        action = "${request.employeeName} Changed the products with total from ${oldSale.totalAmount} to ${request.sale.totalAmount}"
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
                            ))
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
                oldTransactionRef.get().await().toObject(IngotTransaction::class.java) ?: IngotTransaction()
            inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("type", InventoryType.INGOT),
                        Filter.equalTo("karat", request.transaction.karat),
                        Filter.equalTo("ownerId", request.transaction.distributorId)
                    )
                ).get().await().documents.firstOrNull()?.reference?.let { ref ->
                    val oldAmount = if(oldTransaction.type == TransactionType.SALE) oldTransaction.grams.unaryMinus() else oldTransaction.grams
                    val newAmount = if(request.transaction.type == TransactionType.SALE) request.transaction.grams.unaryMinus() else request.transaction.grams
                    val updates = mapOf(
                        "quantity" to FieldValue.increment(
                            if(request.transaction.deleted) oldAmount else newAmount - oldAmount
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
                    action = "Deleted this sale"
                )
            )
            batch.set(saleRef, sale.copy(logs = logs, deleted = true))
            val customerRef = wholesaleCustomersCollection.document(sale.customerId)
            var totalPaymentDeleted = 0.0
            sale.paymentsIds.forEach {
                val paymentRef = moneyPaymentCollection.document(it)
                val payment = paymentRef.get().await().toObject(MoneyPayment::class.java)
                val log = ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    action = "Deleted this payment"
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
                    action = "Deleted this sale"
                )
            )
            batch.set(saleRef, sale.copy(logs = logs, deleted = true))
            val customerRef = wholesaleCustomersCollection.document(sale.customerId)
            var totalPaymentDeleted = 0.0
            sale.moneyPaymentsIds.forEach {
                val paymentRef = moneyPaymentCollection.document(it)
                val payment = paymentRef.get().await().toObject(MoneyPayment::class.java)
                val log = ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    action = "Deleted this payment"
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
            sale.goldPaymentsIds.forEach {
                val paymentRef = goldPaymentsCollection.document(it)
                val log = ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    action = "Deleted this payment"
                )
                val updatePayments = mapOf(
                    "deleted" to true,
                    "logs" to FieldValue.arrayUnion(log)
                )
                batch.update(paymentRef, updatePayments)
            }
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
            val batch = firestore.batch()
            val docRef = wholesaleProductSalesCollection.document()
            val paymentsIds = mutableListOf<String>()
            val maxProductReceipt = wholesaleProductSalesCollection.orderBy(
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val maxGoldReceiptNumber = wholesaleGoldSalesCollection.orderBy(
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val receiptNumber = maxOf(maxProductReceipt, maxGoldReceiptNumber) + 1
            val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
            request.moneyPayments.forEach {
                val ref = moneyPaymentCollection.document()
                paymentsIds.add(ref.id)

                val amount = if (it.type == PaymentType.FUTURES) {
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
                    it.copy(id = ref.id, amount = amount, receiptNumber = receiptNumber.toString())
                )
            }
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
            Result.failure(e)
        }
    }

    override suspend fun addGoldSale(request: AddWholesaleGoldSaleRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val docRef = wholesaleGoldSalesCollection.document()
            val moneyPaymentsIds = mutableListOf<String>()
            val goldPaymentsIds = mutableListOf<String>()
            val maxProductReceipt = wholesaleProductSalesCollection.orderBy(
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val maxGoldReceiptNumber = wholesaleGoldSalesCollection.orderBy(
                "receiptNumber",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0
            val receiptNumber = maxOf(maxProductReceipt, maxGoldReceiptNumber) + 1
            val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
            request.moneyPayments.forEach {
                val ref = moneyPaymentCollection.document()
                moneyPaymentsIds.add(ref.id)
                val amount = if (it.type == PaymentType.FUTURES) {
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
                    it.copy(id = ref.id, amount = amount, receiptNumber = receiptNumber.toString())
                )
            }
            request.goldPayments.forEach {
                val ref = goldPaymentsCollection.document()
                goldPaymentsIds.add(ref.id)
                val amount = if (it.type == PaymentType.FUTURES) {
                    it.givenGoldAmount.unaryMinus()
                } else {
                    it.givenGoldAmount
                }
                batch.set(
                    ref,
                    it.copy(
                        id = ref.id,
                        givenGoldAmount = amount,
                        receiptNumber = receiptNumber.toString()
                    )
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
                    moneyPaymentsIds = moneyPaymentsIds,
                    goldPaymentsIds = goldPaymentsIds,
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
            ).get().await().documents.firstOrNull()?.toObject(MoneyPayment::class.java)
                ?: MoneyPayment()
            val updatedPaymentIds = mutableListOf<String>()
            val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
            Log.d("finding_the_sex", "existingPayment: ${existingPayment.amount}")
            batch.update(
                customerRef,
                mapOf("debtAmount" to FieldValue.increment(existingPayment.amount.unaryMinus()))
            )
            request.moneyPayments.forEach { payment ->

                if (payment.id.isNotEmpty() && existingPaymentIds.contains(payment.id)) {
                    val paymentRef = moneyPaymentCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = moneyPaymentCollection.document()
                    val amount = if (payment.type == PaymentType.FUTURES) {
                        batch.update(
                            customerRef,
                            mapOf("debtAmount" to FieldValue.increment(payment.amount.unaryMinus()))
                        )
                        payment.amount.unaryMinus()
                    } else {
                        payment.amount
                    }
                    batch.set(newPaymentRef, payment.copy(id = newPaymentRef.id, amount = amount))
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
            val existingMoneyPaymentIds = existingSale.moneyPaymentsIds
            val existingPayment = moneyPaymentCollection.where(
                Filter.and(
                    Filter.inArray("id", existingMoneyPaymentIds),
                    Filter.equalTo("type", PaymentType.FUTURES)
                )
            ).get().await().documents.firstOrNull()?.toObject(MoneyPayment::class.java)
                ?: MoneyPayment()
            val existingGoldPaymentIds = existingSale.goldPaymentsIds
            val updatedMoneyPaymentIds = mutableListOf<String>()
            val updatedGoldPaymentIds = mutableListOf<String>()
            val customerRef = wholesaleCustomersCollection.document(request.sale.customerId)
            batch.update(
                customerRef,
                mapOf("debtAmount" to FieldValue.increment(existingPayment.amount.unaryMinus()))
            )
            request.moneyPayments.forEach { payment ->
                if (payment.id.isNotEmpty() && existingMoneyPaymentIds.contains(payment.id)) {
                    val paymentRef = moneyPaymentCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedMoneyPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = moneyPaymentCollection.document()
                    val amount = if (payment.type == PaymentType.FUTURES) {
                        batch.update(
                            customerRef,
                            mapOf("debtAmount" to FieldValue.increment(payment.amount.unaryMinus()))
                        )
                        payment.amount.unaryMinus()
                    } else {
                        payment.amount
                    }
                    batch.set(newPaymentRef, payment.copy(id = newPaymentRef.id, amount = amount))
                    updatedMoneyPaymentIds.add(newPaymentRef.id)
                }
            }
            existingMoneyPaymentIds.forEach { paymentId ->
                if (!updatedMoneyPaymentIds.contains(paymentId)) {
                    batch.delete(moneyPaymentCollection.document(paymentId))
                }
            }
            batch.set(
                saleDocRef, request.sale.copy(moneyPaymentsIds = updatedMoneyPaymentIds),
                SetOptions.merge()
            )
            //
            request.goldPayments.forEach { payment ->
                if (payment.id.isNotEmpty() && existingGoldPaymentIds.contains(payment.id)) {
                    val paymentRef = goldPaymentsCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedGoldPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = goldPaymentsCollection.document()
                    batch.set(newPaymentRef, payment.copy(id = newPaymentRef.id))
                    updatedGoldPaymentIds.add(newPaymentRef.id)
                }
            }
            existingGoldPaymentIds.forEach { paymentId ->
                if (!updatedGoldPaymentIds.contains(paymentId)) {
                    batch.delete(goldPaymentsCollection.document(paymentId))
                }
            }
            batch.set(
                saleDocRef, request.sale.copy(
                    goldPaymentsIds = updatedGoldPaymentIds,
                    moneyPaymentsIds = updatedMoneyPaymentIds
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
        fun isCustomerDifferent(sale1: StoreSale, sale2: StoreSale): Boolean {
            return sale1.customerName != sale2.customerName || sale1.customerEmail != sale2.customerEmail || sale1.customerPhone != sale2.customerPhone
        }

        fun isProductsDifferent(sale1: StoreSale, sale2: StoreSale): Boolean {
            return sale1.products != sale2.products
        }
    }
}