package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.AddInventoryRequest
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.model.inventory.request.UpdateInventoryRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class InventoryRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : InventoryRemoteSource {
    private val inventoryCollection = firestore.collection("inventory")

    override fun fetchInventories(request: FetchInventoriesRequest): Flow<Result<List<Inventory>>> =
        callbackFlow {
            try {
                inventoryCollection.whereEqualTo("ownerId", request.ownerId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            crashlytics.recordException(e)
                            trySend(Result.failure(e))
                        }
                        val inventory = snapshot?.toObjects(Inventory::class.java) ?: emptyList()
                        trySend(Result.success(inventory))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {}
        }

    override suspend fun addInventory(request: AddInventoryRequest): Result<Unit> {
        return try{
            val batch = firestore.batch()
            val mainInventoryRef = inventoryCollection.document(request.mainInventoryId)
            batch.update(
                mainInventoryRef,
                mapOf(
                    "quantity" to FieldValue.increment(request.inventory.quantity.unaryMinus()),
                    "lastUpdated" to Date()
                )
            )
            val inventoryRef = inventoryCollection.document()
            batch.set(inventoryRef, request.inventory.copy(id = inventoryRef.id))
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateInventory(request: UpdateInventoryRequest): Result<Unit> {
        return try{
            val batch = firestore.batch()
            val mainInventoryRef = inventoryCollection.document(request.mainInventoryId)
            batch.update(
                mainInventoryRef,
                mapOf(
                    "quantity" to FieldValue.increment(request.quantity.unaryMinus())
                )
            )
            val inventoryRef = inventoryCollection.document(request.inventoryId)
            batch.update(
                inventoryRef,
                mapOf(
                    "quantity" to FieldValue.increment(request.quantity)
                )
            )
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}