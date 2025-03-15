package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CategoryRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : CategoryRemoteSource {
    private val categoriesCollection = firestore.collection("categories")
    override fun fetchAllCategories(): Flow<Result<List<Category>>> = callbackFlow {
        try {
            categoriesCollection
                .orderBy(
                    "name"
                ).addSnapshotListener { value, error ->
                    if (error != null) {
                        crashlytics.recordException(error)
                        trySend(Result.failure(error))
                    }
                    val products = value?.toObjects(Category::class.java) ?: emptyList()
                    trySend(Result.success(products))
                }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose { }
    }

    override suspend fun addCategory(category: Category): Result<Unit> {
        try {
            val document = categoriesCollection.document()
            document.set(category.copy(id = document.id)).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}