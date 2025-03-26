package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.model.cheque.request.AddNewSalesChequeRequest
import com.zaed.common.data.model.cheque.request.UpdateChequeStatusRequest
import com.zaed.common.data.model.cheque.toMap
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.domain.cheque.DeleteManagerChequeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChequeRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : ChequeRemoteSource {
    private val managerChequeCollection = firestore.collection("managerCheque")
    private val salesChequeCollection = firestore.collection("payments")
    override suspend fun addNewSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        try {
            salesChequeCollection.add(request.chequePayment)
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun fetchAllUnCashedSalesCheque(): Result<List<ChequePayment>> {
        try {
            val result = salesChequeCollection.where(
                Filter.and(
                    Filter.equalTo("deleted", false),
                    Filter.equalTo("chequeStatus", ChequeStatus.RECEIVED),
                    Filter.equalTo("type", PaymentType.CHEQUE)
                )
            ).get().await()
            val cheques = result?.toObjects(ChequePayment::class.java) ?: emptyList()
            return Result.success(cheques)

        } catch (e: Exception) {
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
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        try {
            salesChequeCollection.document(request.chequePayment.id).set(
                request.chequePayment
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun updateSalesCheckStatus(request: UpdateChequeStatusRequest): Result<Unit> {
        try {
            salesChequeCollection.document(request.chequeId).update(
                "chequeStatus", request.status
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override fun fetchManagerCheques(): Flow<Result<List<ManagerCheque>>> = callbackFlow {
        val listener = salesChequeCollection.where(
            Filter.and(
                Filter.equalTo("deleted", false),
                Filter.equalTo("type", PaymentType.MANAGER_CHEQUES)
            )
        ).addSnapshotListener { value, error ->
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

    override fun fetchSalesCheques(): Flow<Result<List<ChequePayment>>> = callbackFlow {
        val listener = salesChequeCollection.where(
            Filter.and(
                Filter.equalTo("deleted", false),
                Filter.equalTo("type", PaymentType.CHEQUE)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                crashlytics.recordException(error)
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            val cheques = value?.toObjects(ChequePayment::class.java) ?: emptyList()
            trySend(Result.success(cheques))
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun deleteManagerCheque(request: DeleteManagerChequeRequest): Result<Unit> {
        try {
            managerChequeCollection.document(request.managerCheque.id).delete()
            //update supplier amount
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override suspend fun addNewManagerCheque(request: AddNewManagerChequeRequest): Result<Unit> {
        try {
            managerChequeCollection.add(request.managerCheque)
            //update supplier amount
            return Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }
}