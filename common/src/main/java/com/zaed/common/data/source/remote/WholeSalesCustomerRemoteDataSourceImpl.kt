package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.domain.payment.UpdateCustomerDebtRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WholeSalesCustomerRemoteDataSourceImpl(
    firebaseFirestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : WholeSalesCustomerRemoteDataSource {
    private val customersCollection = firebaseFirestore.collection("whole_sale_customers")
    override fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>> = callbackFlow {
        try {
            customersCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    crashlytics.recordException(error)
                    trySend(Result.failure(error))
                } else {
                    val customers =
                        snapshot?.toObjects(WholeSaleCustomer::class.java) ?: emptyList()
                    trySend(Result.success(customers))
                }
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            trySend(Result.failure(e))
        }
        awaitClose { }
    }

    override suspend fun updateCustomerDebt(updateCustomerDebtRequest: UpdateCustomerDebtRequest): Result<Unit> {
        try {
            customersCollection.document(updateCustomerDebtRequest.customerId).update(
                "debtAmount", FieldValue.increment(updateCustomerDebtRequest.amount * -1),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        try {
            customersCollection.document(request.customerId).update(
                "debtAmount", FieldValue.increment(request.amount),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholeSaleCustomer(customerId: String): Result<WholeSaleCustomer> {
        try {
            val result = customersCollection.document(customerId).get().await()
            val customer = result.toObject(WholeSaleCustomer::class.java)
            return if (customer != null) {
                Result.success(customer)
            } else {
                Result.failure(Exception("Customer not found"))
            }

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }


    override suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit> {
        try {
            val document = customersCollection.document()
            document.set(
                WholeSaleCustomer(
                    id = document.id,
                    name = request.name,
                    phone = request.phone,
                    email = request.email,
                    address = request.address,
                    city = request.city,
                    zone = request.zone,
                )
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}