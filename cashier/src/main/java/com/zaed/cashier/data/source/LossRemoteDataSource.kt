package com.zaed.cashier.data.source

import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.domain.loss.CreateNewLossRequest
import com.zaed.cashier.domain.loss.GetAllLossesRequest
import kotlinx.coroutines.flow.Flow

interface LossRemoteDataSource {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    fun getAllLosses(request: GetAllLossesRequest): Flow<Result<List<Loss>>>

}
