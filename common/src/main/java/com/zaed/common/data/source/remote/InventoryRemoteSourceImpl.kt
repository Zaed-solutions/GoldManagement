package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.FetchStoreInventoryRequest
import com.zaed.common.data.model.loss.StoreLoss
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InventoryRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : InventoryRemoteSource {
    private val inventoryCollection = firestore.collection("inventory")

    override fun fetchStoreInventory(request: FetchStoreInventoryRequest): Flow<Result<List<Inventory>>> =
        callbackFlow {
            try {
                inventoryCollection.whereEqualTo("ownerId", request.storeId)
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
}