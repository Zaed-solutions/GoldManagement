package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SaleRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : SaleRemoteSource {
    private val storeSalesCollection = firestore.collection("store_sales")
    override fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>> = callbackFlow {
        try{
            storeSalesCollection.whereEqualTo("storeId", request.storeId).addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    crashlytics.recordException(e)
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }
                val storeSales = snapshot?.toObjects(StoreSale::class.java) ?: emptyList()
                trySend(Result.success(storeSales))
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        } finally {
            awaitClose { }
        }
    }
}