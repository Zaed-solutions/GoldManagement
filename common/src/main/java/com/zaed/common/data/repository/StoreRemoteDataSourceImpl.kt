package com.zaed.common.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.Store
import kotlinx.coroutines.tasks.await

class StoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : StoreRemoteDataSource {
    private val STORE_COLLECTION = "stores"
    private val storesCollection = firestore.collection(STORE_COLLECTION)
    override suspend fun getStores(): Result<List<Store>> {
        return try {
            val snapshot = storesCollection.get().await()
            val stores = snapshot.toObjects(Store::class.java)
            Result.success(stores)
        }catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}