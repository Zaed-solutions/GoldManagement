package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.request.AddCategoryRequest
import com.zaed.common.data.model.category.request.DeleteCategoryRequest
import com.zaed.common.data.model.category.request.UpdateCategoryRequest
import com.zaed.common.data.model.inventory.Inventory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class CategoryRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : CategoryRemoteSource {
    private val categoriesCollection = firestore.collection("categories")
    private val inventoryCollection = firestore.collection("inventory")
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

    override suspend fun addCategory(request: AddCategoryRequest): Result<Unit> {
        try {
            val batch = firestore.batch()
            val document = categoriesCollection.document()
            batch.set(document, request.category.copy(id = document.id))
            val inventoryRef = inventoryCollection.document()
            batch.set(
                inventoryRef,
                Inventory(
                    id = inventoryRef.id,
                    productId = document.id,
                    productName = request.category.name,
                    quantity = request.availableAmount,
                )
            )
            batch.commit().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun updateCategory(request: UpdateCategoryRequest): Result<Unit> {
        try {
            val batch = firestore.batch()
            val document = categoriesCollection.document(request.category.id)
            batch.set(document, request.category)
            val inventoryRef = inventoryCollection
                .where(
                    Filter.and(
                        Filter.equalTo("productId", request.category.id),
                        Filter.equalTo("ownerId", "")
                    )
                ).get().await().first().reference
            batch.update(
                inventoryRef,
                mapOf(
                    "quantity" to request.availableAmount,
                    "productName" to request.category.name,
                    "lastUpdated" to Date()
                )
            )
            batch.commit().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deleteCategory(request: DeleteCategoryRequest): Result<Unit> {
        try {
            val batch = firestore.batch()
            batch.delete(categoriesCollection.document(request.categoryId))
            val inventoryRef = inventoryCollection.where(
                Filter.and(
                    Filter.equalTo("productId", request.categoryId),
                    Filter.equalTo("ownerId", "")
                )
            ).get().await().first().reference
            batch.delete(inventoryRef)
            batch.commit().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}