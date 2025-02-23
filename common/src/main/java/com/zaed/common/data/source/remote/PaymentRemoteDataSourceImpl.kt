package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : PaymentRemoteDataSource {
    private val paymentsCollection = firestore.collection("payments")
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<String> {
        try {
            val document = paymentsCollection.document()
            val amount = if(request.moneyPayment.type == PaymentType.FUTURES){
                request.moneyPayment.amount.unaryMinus()
            }else{
                request.moneyPayment.amount
            }
            document.set(
                MoneyPayment(
                    id = document.id,
                    customerId = request.customerId,
                    amount = amount,
                    type = request.moneyPayment.type,
                )
            ).await()
            return Result.success(document.id)
        }catch (e: Exception){
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }
    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<MoneyPayment>>> =
        callbackFlow {
            try {
                paymentsCollection.whereEqualTo("customerId", request.customerId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            crashlytics.recordException(error)
                            trySend(Result.failure(error))
                        } else {
                            val payments =
                                snapshot?.toObjects(MoneyPayment::class.java) ?: emptyList()
                            trySend(Result.success(payments))
                        }
                    }
                paymentsCollection.whereEqualTo("customerId", request.customerId)
            } catch (e: Exception) {
                crashlytics.recordException(e)
                e.printStackTrace()
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun fetchPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<MoneyPayment>> {
        return try {
            val moneyPayments = paymentsCollection.whereIn("id", request.paymentsIds).get().await().toObjects<MoneyPayment>()
            Result.success(moneyPayments)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun editPayment(request: EditPaymentRequest): Result<Unit> {
        try {
            paymentsCollection.document(request.newMoneyPayment.id).set(request.newMoneyPayment).await()
            return Result.success(Unit)
        }catch (e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deletePayment(id: String): Result<Unit> {
        try {
            paymentsCollection.document(id).delete().await()
            return Result.success(Unit)
        }catch (e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}