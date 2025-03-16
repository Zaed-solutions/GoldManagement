package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.purchase.request.FetchSupplierPurchasesRequest
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PurchaseRemoteDataSourceImpl(
    firebaseFirestore: FirebaseFirestore,
    val crashlytics: FirebaseCrashlytics
) : PurchaseRemoteDataSource {
    private val purchaseCollection = firebaseFirestore.collection("purchases")
    override suspend fun fetchPurchaseById(id: String): Result<Purchase> {
        try {
            val result = purchaseCollection.document(id).get().await().toObject(Purchase::class.java)?:Purchase()
            return Result.success(result)

        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun addPurchase(request: AddPurchaseRequest): Result<String> {
        try {
            val documentRef = purchaseCollection.document()
            val purchase = request.purchase.copy(id = documentRef.id)
            documentRef.set(purchase).await()
            return Result.success(documentRef.id)
            //update payments
        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String> {
        try {
            val documentRef = purchaseCollection.document(request.purchase.id)
            documentRef.set(request.purchase).await()
            return Result.success(documentRef.id)
            //update payments

        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override fun fetchSupplierPurchases(request: FetchSupplierPurchasesRequest): Flow<Result<List<Purchase>>>
    = callbackFlow {
        try {
            purchaseCollection
                .where(
                    Filter.and(
                        Filter.equalTo("supplierId", request.supplierId),
                        Filter.equalTo("deleted", false)
                    )
                )
                .addSnapshotListener{ value, error ->
                    if (error != null){
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val purchases = value?.toObjects(Purchase::class.java)?: emptyList()
                    trySend(Result.success(purchases))
                }
        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            trySend(Result.failure(e))
        }
        awaitClose {  }
    }

    override fun fetchPurchases(): Flow<Result<List<Purchase>>> = callbackFlow {
        try {
            purchaseCollection
                .whereEqualTo("deleted", false)
                .addSnapshotListener{ value, error ->
                    if (error != null){
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val purchases = value?.toObjects(Purchase::class.java)?: emptyList()
                    trySend(Result.success(purchases))
                }
        } catch (e: Exception){
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose {}
    }
}