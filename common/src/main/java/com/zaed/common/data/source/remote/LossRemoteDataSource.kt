package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.request.CreateNewLossRequest
import com.zaed.common.data.model.request.DeleteLossRequest
import com.zaed.common.data.model.request.GetStoreLossesRequest
import com.zaed.common.data.model.request.UpdateLossRequest
import kotlinx.coroutines.flow.Flow

interface LossRemoteDataSource {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    suspend fun updateLoss(request: UpdateLossRequest): Result<Unit>
    suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit>
    fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<Loss>>>
}
