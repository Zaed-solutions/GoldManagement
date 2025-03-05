package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
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

    override suspend fun addStore(request: AddStoreRequest): Result<Unit> {
        return try{
            val document = storesCollection.document()
            document.set(request.store.copy(id = document.id)).await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateStore(request: UpdateStoreRequest): Result<Unit> {
        return try{
            storesCollection.document(request.store.id).set(request.store).await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteStore(request: DeleteStoreRequest): Result<Unit> {
        return try{
            storesCollection.document(request.store.id).delete().await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun fetchStoreById(request: FetchStoreByIdRequest): Result<Store> {
        return try{
            val document = storesCollection.document(request.storeId).get().await()
            val store = document.toObject(Store::class.java)
            if(store != null){
                Result.success(store)
            } else {
                Result.failure(Exception("Store not found"))
            }
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}