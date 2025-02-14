package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProductRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : ProductRemoteSource {
    private val productsCollection = firestore.collection("products")
    override fun fetchAllProducts(): Flow<Result<List<Product>>> = callbackFlow {
        try{
            productsCollection.addSnapshotListener { value, error ->
                if (error != null) {
                    crashlytics.recordException(error)
                    trySend(Result.failure(error))
                }
                val products = value?.toObjects(Product::class.java)?: emptyList()
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