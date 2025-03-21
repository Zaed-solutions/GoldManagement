package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.signedAmount
import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.FetchWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class PurchaseRemoteDataSourceImpl(
    val firestore: FirebaseFirestore,
    val crashlytics: FirebaseCrashlytics
) : PurchaseRemoteDataSource {
    private val purchaseCollection = firestore.collection("wholesale_purchases")
    private val suppliersCollection = firestore.collection("suppliers")
    private val paymentCollection = firestore.collection("payments")
    private val inventoryCollection = firestore.collection("inventory")
    override suspend fun fetchPurchaseById(id: String): Result<WholesaleTransaction> {
        try {
            val result = purchaseCollection.document(id).get().await()
            return Result.success(
                result.toObject(WholesaleTransaction::class.java) ?: WholesaleTransaction()
            )
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }


    override suspend fun fetchPurchase(request: FetchWholesaleRequest): Result<WholesaleTransaction> {
        return try {
            val sale = purchaseCollection.document(request.id).get().await()
                .toObject(WholesaleTransaction::class.java) ?: WholesaleTransaction()
            Result.success(sale)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }


    override suspend fun deletePurchase(request: DeleteWholesaleRequest): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val purchaseRef = purchaseCollection.document(request.id)
            val purchase = purchaseRef.get().await()
                .toObject(WholesaleTransaction::class.java) ?: WholesaleTransaction()
            val logs = purchase.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.distributorId,
                    employeeName = request.distributorName,
                    type = LogType.DELETE
                )
            )
            batch.set(purchaseRef, purchase.copy(logs = logs, deleted = true))
            if (purchase.customerId.isNotEmpty()) {
                val supplierRef = suppliersCollection.document(purchase.customerId)
                var totalPaymentDeleted = 0.0
                purchase.paymentsIds.forEach {
                    val paymentRef = paymentCollection.document(it)
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
                        supplierRef,
                        mapOf("debtAmount" to FieldValue.increment(totalPaymentDeleted))
                    )
                }
            }
            val inventoryChanges = purchase.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val distributorId = purchase.distributorId
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
                    val updates = mapOf("quantity" to FieldValue.increment(netChange.unaryMinus()))
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

    override suspend fun addPurchase(request: AddPurchaseRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val docRef = purchaseCollection.document()
            val paymentsIds = mutableListOf<String>()
            val lastSale = purchaseCollection.orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).limit(1).get().await().documents.firstOrNull()?.getString("receiptNumber")
                ?.toLongOrNull() ?: 0L

            val receiptNumber = lastSale +1L
            request.payments.filter { it.type==PaymentType.CHEQUE }.forEach {
                paymentsIds.add(it.id)
                val paymentRef = paymentCollection.document(it.id)
                paymentsIds.add(it.id)
                batch.update(
                    paymentRef,
                    mapOf("cashed" to true)
                )
            }
            request.payments.filter { it.type!=PaymentType.CHEQUE }.forEach {
                val ref = paymentCollection.document()
                paymentsIds.add(ref.id)

                if (it.type == PaymentType.FUTURES) {
                    val customerRef = suppliersCollection.document(request.purchase.customerId)
                    batch.update(
                        customerRef,
                        mapOf("debtAmount" to FieldValue.increment(it.amount.unaryMinus()))
                    )
                }
                when (it) {
                    is ManagerCheque -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )

                    is CashPayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )

                    is FuturePayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )


                    is BankTransferPayment -> batch.set(
                        ref,
                        it.copy(id = ref.id, receiptNumber = receiptNumber.toString())
                    )
                }
            }
            val categoryUpdates = request.purchase.products
                .groupBy { it.categoryId }
            val inventoryQuery = inventoryCollection
                .where(
                    Filter.and(
                        Filter.inArray("productId", categoryUpdates.keys.toList()),
                        Filter.equalTo("ownerId", "")
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryQuery.documents.associateBy {
                it.getString("productId") ?: ""
            }
            categoryUpdates.forEach { (categoryId, products) ->
                val inventoryDoc = inventoryByProductId[categoryId]
                if (inventoryDoc != null) {
                    val updates = mapOf(
                        "quantity" to FieldValue.increment(products.sumOf { it.grams }),
                        "buyingPrice" to products.maxOf { it.gramPrice }
                    )
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            Log.d("add_sale", "invoke remote3: $receiptNumber, $paymentsIds")
            batch.set(
                docRef,
                request.purchase.copy(
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

    override suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String> {
        return try {
            val batch = firestore.batch()
            val purchaseDocRef = purchaseCollection.document(request.purchase.id)
            val existingPurchase =
                purchaseCollection.document(request.purchase.id).get().await()
                    .toObject(WholesaleTransaction::class.java)
                    ?: return Result.failure(Exception("Purchase not found"))
            val existingPaymentIds = existingPurchase.paymentsIds
            val existingPayment = paymentCollection.where(
                Filter.and(
                    Filter.inArray("id", existingPaymentIds),
                    Filter.equalTo("type", PaymentType.FUTURES)
                )
            ).get().await().documents.firstOrNull()?.toObject(CashPayment::class.java)
                ?: CashPayment()
            val updatedPaymentIds = mutableListOf<String>()
            if (request.purchase.customerId.isNotBlank()) {
                val supplierRef = suppliersCollection.document(request.purchase.customerId)

                batch.update(
                    supplierRef,
                    mapOf(
                        "debtAmount" to FieldValue.increment(
                            existingPayment.signedAmount().unaryMinus()
                        )
                    )
                )
            }
            request.payments.forEach { payment ->

                if (payment.id.isNotEmpty() && existingPaymentIds.contains(payment.id)) {
                    val paymentRef = paymentCollection.document(payment.id)
                    batch.set(paymentRef, payment, SetOptions.merge())
                    updatedPaymentIds.add(payment.id)
                } else {
                    val newPaymentRef = paymentCollection.document()
                    val amount = if (payment.type == PaymentType.FUTURES) {
                        val supplierRef =
                            suppliersCollection.document(request.purchase.customerId)
                        batch.update(
                            supplierRef,
                            mapOf("debtAmount" to FieldValue.increment(payment.signedAmount()))
                        )
                        payment.amount
                    } else {
                        payment.amount
                    }
                    when (payment) {
                        is ManagerCheque -> batch.set(
                            newPaymentRef,
                            payment.copy(id = newPaymentRef.id, amount = amount)
                        )

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
                    batch.delete(paymentCollection.document(paymentId))
                }
            }
            val oldProductsByCategory = existingPurchase.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
            val newProductsByCategory = request.purchase.products
                .groupBy { it.categoryId }
                .mapValues { (_, products) -> products.sumOf { it.grams } }
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
                        Filter.equalTo("ownerId", "")
                    )
                )
                .get().await()

            val inventoryByProductId = inventoryRefs.documents.associateBy {
                it.getString("productId") ?: ""
            }
            inventoryChanges.forEach { (categoryId, netChange) ->
                val inventoryDoc = inventoryByProductId[categoryId]

                if (inventoryDoc != null) {
                    val updates = mapOf("quantity" to FieldValue.increment(netChange.unaryMinus()))
                    batch.update(inventoryDoc.reference, updates)
                }
            }
            batch.set(
                purchaseDocRef, request.purchase.copy(paymentsIds = updatedPaymentIds),
                SetOptions.merge()
            )
            batch.commit().await()
            Result.success(purchaseDocRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun fetchSupplierPurchases(request: FetchSupplierPurchasesRequest): Flow<Result<List<WholesaleTransaction>>>
    = callbackFlow {
        try {
            val filter = if(request.isManager){
                    Filter.equalTo("customerId", request.supplierId)
            } else {
                Filter.and(
                    Filter.equalTo("customerId", request.supplierId),
                    Filter.equalTo("deleted", false)
                )
            }
            purchaseCollection
                .where(
                    filter
                )
                .addSnapshotListener{ value, error ->
                    if (error != null){
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val purchases = value?.toObjects(WholesaleTransaction::class.java)?: emptyList()
                    trySend(Result.success(purchases))
                }
        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            trySend(Result.failure(e))
        }
        awaitClose {  }
    }

    override fun fetchPurchases(): Flow<Result<List<WholesaleTransaction>>> = callbackFlow {
        try {
            purchaseCollection
                .whereEqualTo("deleted", false)
                .addSnapshotListener{ value, error ->
                    if (error != null){
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val purchases = value?.toObjects(WholesaleTransaction::class.java)?: emptyList()
                    trySend(Result.success(purchases))
                }
        } catch (e: Exception){
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose {}
    }
}