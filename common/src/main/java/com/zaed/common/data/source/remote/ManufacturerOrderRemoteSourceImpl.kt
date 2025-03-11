package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.data.model.manufacturerorder.request.AddManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.DeleteManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.UpdateManufacturerOrderRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ManufacturerOrderRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : ManufacturerOrderRemoteSource {
    private val manufacturerOrdersCollections = firestore.collection("manufacturer-orders")
    override fun fetchManufacturerOrders(): Flow<Result<List<ManufacturerOrder>>> =
        callbackFlow {
            try {
                manufacturerOrdersCollections
                    .orderBy(
                        "createdAt",
                        Query.Direction.DESCENDING
                    ).addSnapshotListener { value, error ->
                        if (error != null) {
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        val manufacturerOrders =
                            value?.toObjects(ManufacturerOrder::class.java) ?: emptyList()
                        trySend(Result.success(manufacturerOrders))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun addManufacturerOrder(request: AddManufacturerOrderRequest): Result<Unit> {
        return try {
            val docRef = manufacturerOrdersCollections.document()
            val maxOrderNumber = manufacturerOrdersCollections
                .orderBy(
                    "createdAt",
                    Query.Direction.DESCENDING
                ).limit(1)
                .get().await().documents.firstOrNull()?.getString("orderNumber")?.toLongOrNull()?:0
            docRef.set(request.order.copy(id = docRef.id, orderNumber = (maxOrderNumber + 1).toString())).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateManufacturerOrder(request: UpdateManufacturerOrderRequest): Result<Unit> {
        return try {
            manufacturerOrdersCollections.document(request.order.id).set(request.order).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteManufacturerOrder(request: DeleteManufacturerOrderRequest): Result<Unit> {
        return try {
            manufacturerOrdersCollections.document(request.order.id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}