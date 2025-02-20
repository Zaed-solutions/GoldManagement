package com.zaed.common.data.repository

import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import com.zaed.common.data.source.remote.LossRemoteDataSource
import kotlinx.coroutines.flow.Flow

class LossRepositoryImpl(
    private val lossRemoteDataSource: LossRemoteDataSource
) : LossRepository {
    override suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit> {
        return lossRemoteDataSource.createNewLoss(request)
    }

    override suspend fun updateLoss(request: UpdateLossRequest): Result<Unit> {
        return lossRemoteDataSource.updateLoss(request)
    }

    override suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit> {
        return lossRemoteDataSource.deleteLoss(request)
    }

    override fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<Loss>>> {
        return lossRemoteDataSource.getStoreLosses(request)
    }
}