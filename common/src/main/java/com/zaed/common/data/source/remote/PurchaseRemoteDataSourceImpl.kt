package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.coroutines.tasks.await

class PurchaseRemoteDataSourceImpl(
    firebaseFirestore: FirebaseFirestore,
    val crashlytics: FirebaseCrashlytics
) : PurchaseRemoteDataSource {
    private val purchaseCollection = firebaseFirestore.collection("purchases")
    override suspend fun fetchPurchaseById(id: String): Result<WholesaleTransaction> {
        try {
            val result = purchaseCollection.document(id).get().await()
            if(ProductType.valueOf(result["productType"].toString()) == ProductType.PRODUCT){
                return Result.success(result.toObject(WholesaleTransaction::class.java)?:WholesaleTransaction())
            }else{
                return Result.success(result.toObject(WholesaleTransaction::class.java)?:WholesaleTransaction())
            }
        }catch ( e: Exception){
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun addPurchase(request: AddPurchaseRequest): Result<String> {
        try {
            val documentRef = purchaseCollection.document()
            documentRef.set(request.purchase.copy(id = documentRef.id)).await()
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
}