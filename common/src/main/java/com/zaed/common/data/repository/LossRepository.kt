package com.zaed.common.data.repository

import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import kotlinx.coroutines.flow.Flow

interface LossRepository {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    suspend fun updateLoss(request: UpdateLossRequest): Result<Unit>
    suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit>
    fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<Loss>>>
}
