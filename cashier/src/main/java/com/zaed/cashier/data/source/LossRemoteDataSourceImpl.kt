package com.zaed.cashier.data.source

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.domain.loss.CreateNewLossRequest
import com.zaed.cashier.domain.loss.GetAllLossesRequest
import com.zaed.cashier.ui.loss.display.BaseError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LossRemoteDataSourceImpl(
    firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : LossRemoteDataSource {
    private val LOSS_STORE_COLLECTION = "loss-store"
    private val lossCollection = firestore.collection(LOSS_STORE_COLLECTION)
    override suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit> {
        return try {
            val document = lossCollection.document()
            document.set(request).await()
            Result.success(Unit)
        }catch (e:Exception){
            val error = BaseError.Unknown(
                reason = e.message ?: "Unknown error",
            )
            crashlytics.recordException(error)
            crashlytics.log(error.toString())
            error.log()
            Result.failure(error)
        }

    }

    override fun getAllLosses(request: GetAllLossesRequest): Flow<Result<List<Loss>>> = callbackFlow {
        try {
            lossCollection.addSnapshotListener{ snapshot, e ->
                if(e != null){
                    val error = BaseError.Unknown(
                        reason = e.message ?: "Unknown error",
                    )
                    error.log()
                    crashlytics.recordException(error)
                    crashlytics.log(error.toString())
                    trySend(Result.failure(error))
                }
                if(snapshot != null && !snapshot.isEmpty){
                    val losses = snapshot.toObjects(Loss::class.java)
                    trySend(Result.success(losses))
                }else{
                    trySend(Result.success(emptyList()))
                }
            }

        }catch (e:Exception){
            val error = BaseError.Unknown(
                reason = e.message ?: "Unknown error",
            )
            crashlytics.recordException(error)
            crashlytics.log(error.toString())
            error.log()
            trySend(Result.failure(error))
        }
        awaitClose {}
    }
}