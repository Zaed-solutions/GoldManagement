package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CategoryRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : CategoryRemoteSource {
    private val categoriesCollection = firestore.collection("categories")
    override fun fetchAllCategories(): Flow<Result<List<Category>>> = callbackFlow {
        try{
            categoriesCollection.addSnapshotListener { value, error ->
                if (error != null) {
                    crashlytics.recordException(error)
                    trySend(Result.failure(error))
                }
                val products = value?.toObjects(Category::class.java)?: emptyList()
                trySend(Result.success(products))
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        } finally {
            awaitClose {  }
        }
    }
}