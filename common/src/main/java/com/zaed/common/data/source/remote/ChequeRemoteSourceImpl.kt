package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.cheque.SalesCheque
import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.model.cheque.request.AddNewSalesChequeRequest
import com.zaed.common.data.model.cheque.request.UpdateChequeStatusRequest
import com.zaed.common.data.model.cheque.toMap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChequeRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : ChequeRemoteSource {
    private val managerChequeCollection = firestore.collection("managerCheque")
    private val salesChequeCollection = firestore.collection("salesCheque")
    override suspend fun addNewSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        try {
            salesChequeCollection.add(request.salesCheque)
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateManagerCheque(request: AddNewManagerChequeRequest): Result<Unit> {
        try {
            managerChequeCollection.document(request.managerCheque.id).update(
                request.managerCheque.toMap()
            ).await()
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        try {
            salesChequeCollection.document(request.salesCheque.id).update(
                request.salesCheque.toMap()
            ).await()
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateSalesCheckStatus(request: UpdateChequeStatusRequest): Result<Unit> {
        try {
            salesChequeCollection.document(request.chequeId).update(
                "chequeStatus",request.status
            ).await()
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateManagerCheckStatus(request: UpdateChequeStatusRequest): Result<Unit> {
        try {
            managerChequeCollection.document(request.chequeId).update(
                "chequeStatus", request.status
            ).await()
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override fun fetchManagerCheques(): Flow<Result<List<ManagerCheque>>> = callbackFlow {
        val listener = managerChequeCollection.addSnapshotListener { value, error ->
            if (error != null) {
                crashlytics.recordException(error)
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            val cheques = value?.toObjects(ManagerCheque::class.java) ?: emptyList()
            trySend(Result.success(cheques))
        }
        awaitClose {
            listener.remove()
        }
    }

    override fun fetchSalesCheques(): Flow<Result<List<SalesCheque>>> = callbackFlow {
        val listener = salesChequeCollection.addSnapshotListener { value, error ->
            if (error != null) {
                crashlytics.recordException(error)
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            val cheques = value?.toObjects(SalesCheque::class.java) ?: emptyList()
            trySend(Result.success(cheques))
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addNewManagerCheque(request: AddNewManagerChequeRequest): Result<Unit> {
        try {
            managerChequeCollection.add(request.managerCheque)
            return Result.success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }
}