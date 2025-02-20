package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
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
    private val LOSS_STORE_COLLECTION = "store-losses"
    private val lossCollection = firestore.collection(LOSS_STORE_COLLECTION)
    override suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit> {
        return try {
            val document = lossCollection.document()
            val loss = request.loss.copy(
                id = document.id,
                logs = listOf(
                    ChangeLog(
                        date = Date(),
                        employeeId = request.loss.userId,
                        employeeName = request.loss.userName,
                        action = "${request.loss.userName} Created a new loss with value ${request.loss.value} and reason ${request.loss.reason}"
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
            lossCollection.document(request.loss.id).set(request.loss).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit> {
        return try {
            val loss = lossCollection.document(request.lossId).get().await().toObject(Loss::class.java) ?: Loss()
            val logs = loss.logs.toMutableList()
            logs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = request.employeeId,
                    employeeName = request.employeeName,
                    action = "${request.employeeName} Deleted this loss"
                )
            )
            lossCollection.document(request.lossId).set(loss.copy(logs = logs, deleted = true)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    override fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<Loss>>> =
        callbackFlow {
            try {
                lossCollection.where(
                    Filter.and(
                        Filter.equalTo("storeId", request.storeId),
                        Filter.equalTo("deleted", false)
                    )
                ).addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            crashlytics.recordException(e)
                            trySend(Result.failure(e))
                        }
                        val losses = snapshot?.toObjects(Loss::class.java)?: emptyList()
                        trySend(Result.success(losses))
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose {}
        }
}