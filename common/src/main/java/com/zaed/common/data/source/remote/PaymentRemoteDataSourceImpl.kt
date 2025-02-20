package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : PaymentRemoteDataSource {
    private val paymentsCollection = firestore.collection("payments")
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit> {
        try {
            val document = paymentsCollection.document()
            document.set(
                Payment(
                    id = document.id,
                    customerId = request.customerId,
                    amount = request.payment.amount,
                    type = request.payment.type,
                )
            ).await()
            return Result.success(Unit)
        }catch (e: Exception){
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }
    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> =
        callbackFlow {
            try {
                paymentsCollection.whereEqualTo("customerId", request.customerId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            crashlytics.recordException(error)
                            trySend(Result.failure(error))
                        } else {
                            val payments =
                                snapshot?.toObjects(Payment::class.java) ?: emptyList()
                            trySend(Result.success(payments))
                        }
                    }

            } catch (e: Exception) {
                crashlytics.recordException(e)
                e.printStackTrace()
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
}