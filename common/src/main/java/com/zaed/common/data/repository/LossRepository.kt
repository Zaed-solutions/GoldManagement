package com.zaed.common.data.repository

import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.request.CreateNewLossRequest
import com.zaed.common.data.model.request.GetAllLossesRequest
import kotlinx.coroutines.flow.Flow

interface LossRepository {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    fun getAllLosses(request: GetAllLossesRequest): Flow<Result<List<Loss>>>
}
