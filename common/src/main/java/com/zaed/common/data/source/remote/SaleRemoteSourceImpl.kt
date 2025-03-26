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
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.TransactionType
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddIngotTransactionRequest
import com.zaed.common.data.model.sale.request.AddStoreSaleRequest
import com.zaed.common.data.model.sale.request.AddWholesaleRequest
import com.zaed.common.data.model.sale.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.sale.request.FetchIngotTransactionsRequest
import com.zaed.common.data.model.sale.request.FetchStoreSalesRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdateIngotTransactionRequest
import com.zaed.common.data.model.sale.request.UpdateStoreSaleRequest
import com.zaed.common.data.model.sale.request.UpdateWholesaleRequest
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
    private val wholesalesCollection = firestore.collection("wholesale_sales")
    private val ingotTransactionsCollection = firestore.collection("ingot_transactions")
    private val moneyPaymentCollection = firestore.collection("payments")
    private val wholesaleCustomersCollection = firestore.collection("whole_sale_customers")
    private val inventoryCollection = firestore.collection("inventory")
    private val distributorLossesCollection = firestore.collection("distributor-losses")

    override fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreTransaction>>> =
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
                    val storeSales =
                        snapshot?.toObjects(StoreTransaction::class.java) ?: emptyList()
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
                .toObject(StoreTransaction::class.java) ?: StoreTransaction()
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
                .toObject(StoreTransaction::class.java) ?: StoreTransaction()
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

    override suspend fun getStoreSale(saleId: String): Result<StoreTransaction> {
        return try {
            val storeSale =
                storeSalesCollection.document(saleId).get().await()
                    .toObject(StoreTransaction::class.java)
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

    override fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleTransaction>>> =
        callbackFlow {
            var sListener: ListenerRegistration? = null
            try {
                sListener = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", request.distributorId),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("outStandingBill", request.withOutStandingBill)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        trySend(Result.failure(error))
                    } else {
                        val sales =
                            snapshot?.toObjects(WholesaleTransaction::class.java) ?: emptyList()
                        trySend(Result.success(sales))
                    }
                }

            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {
                sListener?.remove()
                sListener?.remove()
            }
        }

    override fun fetchWholesaleCustomerSales(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleTransaction>>> =
        callbackFlow {
            var sListener: ListenerRegistration? = null
            try {
                sListener = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("accountId", request.customerId),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("outStandingBill", request.withOutStandingBill)
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        trySend(Result.failure(error))
                    } else {
                        val sales =
                            snapshot?.toObjects(WholesaleTransaction::class.java) ?: emptyList()
                        trySend(Result.success(sales))
                    }
                }

            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {
                sListener?.remove()
            }
        }

    override fun fetchIngotTransaction(request: FetchIngotTransactionsRequest): Flow<Result<List<IngotTransaction>>> =
        callbackFlow {
            try {
                val filter = if (request.distributorId.isBlank()) {
                    null
                } else {
                    Filter.and(
                        Filter.equalTo("distributorId", request.distributorId),
                        Filter.equalTo("deleted", false)
                    )
                }
                val query = filter?.let { ingotTransactionsCollection.where(it) }
                    ?: ingotTransactionsCollection

                query.addSnapshotListener { snapshot, e ->
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

    override fun fetchAllStoreSales(): Flow<Result<List<StoreTransaction>>> =
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
                        val storeSales =
                            snapshot?.toObjects(StoreTransaction::class.java) ?: emptyList()
                        trySend(Result.success(storeSales))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override fun fetchAllDistributorsSales(): Flow<Result<List<WholesaleTransaction>>> =
        callbackFlow {
            var sListener: ListenerRegistration? = null
            try {
                sListener =
                    wholesalesCollection.addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            trySend(Result.failure(error))
                        }
                        val sales =
                            snapshot?.toObjects(WholesaleTransaction::class.java) ?: emptyList()
                        trySend(Result.success(sales))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {
                sListener?.remove()
                sListener?.remove()
            }
        }


    override suspend fun deleteWholesale(request: DeleteWholesaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleRef = wholesalesCollection.document(request.id)
            val sale = saleRef.get().await()
                .toObject(WholesaleTransaction::class.java) ?: WholesaleTransaction()
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
            if (sale.accountId.isNotEmpty()) {
                val customerRef = wholesaleCustomersCollection.document(sale.accountId)
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
                    batch.update(
                        customerRef,
                        mapOf("moneyDebtAmount" to FieldValue.increment(totalPaymentDeleted))
                    )
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

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }


    override suspend fun fetchWholesale(request: FetchWholesaleRequest): Result<WholesaleTransaction> {
        return try {
            val sale = wholesalesCollection.document(request.id).get().await()
                .toObject(WholesaleTransaction::class.java) ?: WholesaleTransaction()
            Result.success(sale)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun addWholesale(request: AddWholesaleRequest): Result<String> {
        Log.d("add_sale", "invoke remote: ${request.payments.map { it.type } }")
        return try {
            Log.d("add_sale", "invoke remote: $request")
            val batch = firestore.batch()
            val docRef = wholesalesCollection.document()
            val paymentsIds = mutableListOf<String>()
            val lastSale = wholesalesCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0L

            val receiptNumber = lastSale + 1L

            request.payments.forEach {
                val ref = moneyPaymentCollection.document()
                paymentsIds.add(ref.id)

                if (it.type == PaymentType.FUTURES) {
                    val customerRef =
                        wholesaleCustomersCollection.document(request.sale.accountId)

                    batch.update(
                        customerRef,
                        mapOf("moneyDebtAmount" to FieldValue.increment(it.amount.unaryMinus()))
                    )

                } else if (it.type == PaymentType.REMAIN) {
                    val customerRef =
                        wholesaleCustomersCollection.document(request.sale.accountId)
                    batch.update(
                        customerRef,
                        mapOf("moneyDebtAmount" to FieldValue.increment(it.amount))
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
                    is GoldPayment -> batch.set(
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


    override suspend fun updateWholesale(request: UpdateWholesaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val saleDocRef = wholesalesCollection.document(request.sale.id)
            val existingSale =
                wholesalesCollection.document(request.sale.id).get().await()
                    .toObject(WholesaleTransaction::class.java)
                    ?: return Result.failure(Exception("Sale not found"))
            val existingPaymentIds = existingSale.paymentsIds
            val existingPayment = moneyPaymentCollection.where(
                Filter.and(
                    Filter.inArray("id", existingPaymentIds),
                    Filter.or(
                    Filter.equalTo("type", PaymentType.FUTURES),
                    Filter.equalTo("type", PaymentType.REMAIN),
                )
                )
            ).get().await().documents.firstOrNull()?.toObject(CashPayment::class.java)
                ?: CashPayment()
            val updatedPaymentIds = mutableListOf<String>()
            if (request.sale.accountId.isNotBlank()) {
                val customerRef = wholesaleCustomersCollection.document(request.sale.accountId)

                Log.d("finding_the_sex", "existingPayment: ${existingPayment.amount}")
                batch.update(
                    customerRef,
                    mapOf(
                        "moneyDebtAmount" to FieldValue.increment(
                            existingPayment.signedAmount().unaryMinus()
                        )
                    )
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
                            wholesaleCustomersCollection.document(request.sale.accountId)
                        batch.update(
                            customerRef,
                            mapOf("moneyDebtAmount" to FieldValue.increment(payment.signedAmount()))
                        )
                        payment.amount
                    }else if (payment.type == PaymentType.REMAIN) {
                        val customerRef =
                            wholesaleCustomersCollection.document(request.sale.accountId)
                        batch.update(
                            customerRef,
                            mapOf("moneyDebtAmount" to FieldValue.increment(payment.signedAmount()))
                        )
                        payment.amount
                    }else {
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
                        is GoldPayment -> batch.set(
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

    companion object {
        fun isProductsDifferent(sale1: StoreTransaction, sale2: StoreTransaction): Boolean {
            return sale1.products != sale2.products
        }
    }
}