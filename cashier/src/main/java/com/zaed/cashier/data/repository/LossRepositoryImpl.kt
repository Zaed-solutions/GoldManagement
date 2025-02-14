package com.zaed.cashier.data.repository

import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.data.source.LossRemoteDataSource
import com.zaed.cashier.domain.loss.CreateNewLossRequest
import com.zaed.cashier.domain.loss.GetAllLossesRequest
import kotlinx.coroutines.flow.Flow

class LossRepositoryImpl(
    private val lossRemoteDataSource: LossRemoteDataSource
) : LossRepository {
    override suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit> {
        return lossRemoteDataSource.createNewLoss(request)
    }

    override fun getAllLosses(request: GetAllLossesRequest): Flow<Result<List<Loss>>> {
        return lossRemoteDataSource.getAllLosses(request)
    }
}