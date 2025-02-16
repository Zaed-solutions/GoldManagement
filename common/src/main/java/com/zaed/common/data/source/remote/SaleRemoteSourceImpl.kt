package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.ChangeLog
import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.data.model.request.UpdateStoreSaleRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class SaleRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : SaleRemoteSource {
    private val storeSalesCollection = firestore.collection("store_sales")
    override fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>> = callbackFlow {
        try{
            storeSalesCollection.where(
                Filter.and(
                    Filter.equalTo("storeId", request.storeId),
                    Filter.equalTo("deleted", false)
                )
            ).addSnapshotListener{ snapshot, e ->
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

    override suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String> {
        return try {
            val docRef = storeSalesCollection.document()
            docRef.set(request.sale.copy(id = docRef.id)).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit> {
        return try {
            val sale = storeSalesCollection.document(request.saleId).get().await().toObject(StoreSale::class.java) ?: StoreSale()
            val logs = sale.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.employeeId,
                    employeeName = request.employeeName,
                    action = "${request.employeeName} Deleted this sale"
                )
            )
            storeSalesCollection.document(request.saleId).set(sale.copy(logs = logs, deleted = true)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit> {
        return try {
            storeSalesCollection.document(request.sale.id).set(request.sale).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun getStoreSale(saleId: String): Result<StoreSale> {
        return try {
            val docRef = storeSalesCollection.document(saleId)
            val snapshot = docRef.get().await()
            val storeSale = snapshot.toObject(StoreSale::class.java)
            if (storeSale != null) {
                Result.success(storeSale)
            } else {
                Result.failure(Exception("Store sale not found"))
            }

        }catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}