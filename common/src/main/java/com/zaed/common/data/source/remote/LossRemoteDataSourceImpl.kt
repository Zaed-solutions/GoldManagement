package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.data.model.loss.request.AddDistributorLossRequest
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateDistributorLossRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class LossRemoteDataSourceImpl(
    firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : LossRemoteDataSource {
    private val storeLossesCollection = firestore.collection("store-losses")
    private val distributorLossesCollection = firestore.collection("distributor-losses")
    override suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit> {
        return try {
            val document = storeLossesCollection.document()
            val loss = request.loss.copy(
                id = document.id,
                logs = listOf(
                    ChangeLog(
                        date = Date(),
                        employeeId = request.loss.userId,
                        employeeName = request.loss.userName,
                        type = LogType.CREATE
                    )
                )
            )
            document.set(loss).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }

    }

    override suspend fun updateLoss(request: UpdateLossRequest): Result<Unit> {
        return try {
            storeLossesCollection.document(request.loss.id).set(request.loss).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit> {
        return try {
            val loss = storeLossesCollection.document(request.lossId).get().await()
                .toObject(StoreLoss::class.java) ?: StoreLoss()
            val logs = loss.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.employeeId,
                    employeeName = request.employeeName,
                    type = LogType.DELETE
                )
            )
            storeLossesCollection.document(request.lossId)
                .set(loss.copy(logs = logs, deleted = true)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<StoreLoss>>> =
        callbackFlow {
            try {
                storeLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("storeId", request.storeId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        crashlytics.recordException(e)
                        trySend(Result.failure(e))
                    }
                    val losses = snapshot?.toObjects(StoreLoss::class.java) ?: emptyList()
                    trySend(Result.success(losses))
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {}
        }

    override fun fetchDistributorLosses(request: FetchDistributorLossesRequest): Flow<Result<List<DistributorLoss>>> =
        callbackFlow {
            try {
                distributorLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("userId", request.distributorId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        crashlytics.recordException(e)
                        trySend(Result.failure(e))
                    }
                    val losses = snapshot?.toObjects(DistributorLoss::class.java) ?: emptyList()
                    trySend(Result.success(losses))
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {}
        }

    override suspend fun updateDistributorLoss(request: UpdateDistributorLossRequest): Result<Unit> {
        return try {
            distributorLossesCollection.document(request.loss.id).set(request.loss).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun addDistributorLoss(request: AddDistributorLossRequest): Result<Unit> {
        return try {
            val document = distributorLossesCollection.document()
            document.set(request.loss.copy(id = document.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun fetchStoreLosses(request: FetchStoreLossesRequest): Flow<Result<List<StoreLoss>>> =
        callbackFlow {
            try {
                storeLossesCollection.whereEqualTo("storeId", request.storeId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            crashlytics.recordException(e)
                            trySend(Result.failure(e))
                        }
                        val losses = snapshot?.toObjects(StoreLoss::class.java) ?: emptyList()
                        trySend(Result.success(losses))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {}
        }
}