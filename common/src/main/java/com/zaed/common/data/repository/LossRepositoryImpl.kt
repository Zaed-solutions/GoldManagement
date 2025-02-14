package com.zaed.common.data.repository

import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.request.CreateNewLossRequest
import com.zaed.common.data.model.request.GetAllLossesRequest
import com.zaed.common.data.source.remote.LossRemoteDataSource
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