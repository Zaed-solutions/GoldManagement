package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.zaed.common.data.model.customer.FetchSuppliersByNameRequest
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SupplierRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : SupplierRemoteSource {
    val suppliersCollection = firestore.collection("suppliers")
    override fun fetchSuppliers(): Flow<Result<List<Supplier>>> = callbackFlow {
        try{
            suppliersCollection
                .orderBy("name")
                .addSnapshotListener { value, error ->
                    if (error != null){
                        crashlytics.recordException(error)
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val suppliers = value?.toObjects(Supplier::class.java) ?: emptyList()
                    trySend(Result.success(suppliers))
                }
        } catch (e: Exception){
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose {  }
    }

    override suspend fun addSupplier(request: AddSupplierRequest): Result<Unit> {
        return try{
            val docRef = suppliersCollection.document()
            docRef.set(request.supplier.copy(id = docRef.id)).await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteSupplier(request: DeleteSupplierRequest): Result<Unit> {
        return try{
            suppliersCollection.document(request.supplierId).delete().await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateSupplier(request: UpdateSupplierRequest): Result<Unit> {
        return try{
            suppliersCollection.document(request.supplier.id).set(request.supplier).await()
            Result.success(Unit)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun fetchSupplier(request: FetchSupplierRequest): Result<Supplier> {
        return try{
            val supplier = suppliersCollection.document(request.supplierId).get().await().toObject(Supplier::class.java)?: throw Exception("Supplier not found")
            Result.success(supplier)
        } catch (e: Exception){
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun fetchSuppliersByName(request: FetchSuppliersByNameRequest): Result<List<Supplier>> {
        return try {
            val filter = if(request.distributorId.isBlank()){
                Filter.and(
                    Filter.greaterThanOrEqualTo("name", request.name),
                    Filter.lessThanOrEqualTo("name", request.name + "\uf8ff")
                )
            } else {
                Filter.and(
                    Filter.greaterThanOrEqualTo("name", request.name),
                    Filter.lessThanOrEqualTo("name", request.name + "\uf8ff"),
                    Filter.equalTo("distributorId", request.distributorId)
                )
            }
            val supplier = suppliersCollection
                .where(
                    filter
                ).get()
                .await()
                .toObjects<Supplier>()
            Result.success(supplier)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}