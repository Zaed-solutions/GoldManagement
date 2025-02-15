package com.zaed.common.data.source.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.request.CreateNewLossRequest
import com.zaed.common.data.model.request.GetAllLossesRequest
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
            val loss = Loss(
                id = document.id,
                value = request.value,
                reason = request.reason,
            )
            document.set(loss).await()
            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }

    }

    override fun getAllLosses(request: GetAllLossesRequest): Flow<Result<List<Loss>>> = callbackFlow {
        try {
            lossCollection.addSnapshotListener{ snapshot, e ->
                if(e != null){
                    trySend(Result.failure(e))
                }
                if(snapshot != null && !snapshot.isEmpty){
                    val losses = snapshot.toObjects(Loss::class.java)
                    trySend(Result.success(losses))
                }else{
                    trySend(Result.success(emptyList()))
                }
            }

        }catch (e:Exception){

            trySend(Result.failure(e))
        }
        awaitClose {}
    }
}