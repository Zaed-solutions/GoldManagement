package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.customer.request.EditWholeSalesCustomerRequest
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.domain.payment.UpdateCustomerDebtRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WholeSalesCustomerRemoteDataSourceImpl(
    firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : WholeSalesCustomerRemoteDataSource {
    private val customersCollection = firestore.collection("whole_sale_customers")
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
                "debtAmount", FieldValue.increment(updateCustomerDebtRequest.difference)
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun addNewPayment(request: AddNewPaymentRequest): Result<Unit> {
        try {
            customersCollection.document(request.customerId).update(
                "debtAmount",
                FieldValue.increment(request.payment.amount),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deleteCustomer(customerId: String): Result<Unit> {
        try {
            customersCollection.document(customerId).delete().await()
            return Result.success(Unit)
        }catch (e:Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun editWholeSalesCustomer(request: EditWholeSalesCustomerRequest): Result<Unit> {
        try {
            customersCollection.document(request.id).update(
                mapOf(
                    "name" to request.updatedData.name,
                    "email" to request.updatedData.email,
                    "phone" to request.updatedData.phone,
                    "address" to request.updatedData.address,
                    "city" to request.updatedData.city,
                    "zone" to request.updatedData.zone,
                )
            ).await()
            return Result.success(Unit)
        }catch (e:Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        try {
            customersCollection.document(request.customerId).update(
                "debtAmount",
                FieldValue.increment(-request.amount),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun fetchWholesaleCustomersByName(request: FetchWholesaleCustomersByNameRequest): Result<List<WholeSaleCustomer>> {
        return try {
            val customers = customersCollection
                .where(
                    Filter.and(
                        Filter.greaterThanOrEqualTo("name", request.name),
                        Filter.lessThanOrEqualTo("name", request.name + "\uf8ff")
                    )
                ).get()
                .await()
                .toObjects<WholeSaleCustomer>()
            Result.success(customers)
        } catch (e: Exception) {
            Result.failure(e)
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
                    distributorId = request.distributorId,
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