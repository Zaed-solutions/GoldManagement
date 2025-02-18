package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WholeSalesCustomerRemoteDataSourceImpl(
    firebaseFirestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : WholeSalesCustomerRemoteDataSource {
    private val customersCollection = firebaseFirestore.collection("customers")
    override fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>> = callbackFlow {
        try {
            customersCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    crashlytics.recordException(error)
                    trySend(Result.failure(error))
                }else {
                    val customers =
                        snapshot?.toObjects(WholeSaleCustomer::class.java) ?: emptyList()
                    trySend(Result.success(customers))
                }
            }

        }catch (e:Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            trySend(Result.failure(e))
        }
        awaitClose {  }
    }

    override suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit> {
        try {
            customersCollection.add(request).await()
            return Result.success(Unit)
        }catch (e:Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}