package com.zaed.common.data.repository

import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.data.model.loss.request.AddDistributorLossRequest
import com.zaed.common.data.model.loss.request.AddManagerLossRequest
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.DeleteManagerLossRequest
import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateDistributorLossRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import com.zaed.common.data.model.loss.request.UpdateManagerLossRequest
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

    override fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<StoreLoss>>> {
        return lossRemoteDataSource.getStoreLosses(request)
    }

    override fun fetchDistributorLosses(request: FetchDistributorLossesRequest): Flow<Result<List<DistributorLoss>>> {
        return lossRemoteDataSource.fetchDistributorLosses(request)
    }

    override suspend fun addDistributorLoss(request: AddDistributorLossRequest): Result<Unit> {
        return lossRemoteDataSource.addDistributorLoss(request)
    }

    override suspend fun updateDistributorLoss(request: UpdateDistributorLossRequest): Result<Unit> {
        return lossRemoteDataSource.updateDistributorLoss(request)
    }

    override fun fetchStoreLosses(request: FetchStoreLossesRequest): Flow<Result<List<StoreLoss>>> {
        return lossRemoteDataSource.fetchStoreLosses(request)
    }

    override fun fetchManagerLosses(): Flow<Result<List<ManagerLoss>>> {
        return lossRemoteDataSource.fetchManagerLosses()
    }

    override suspend fun addManagerLoss(request: AddManagerLossRequest): Result<Unit> {
        return lossRemoteDataSource.addManagerLoss(request)
    }

    override suspend fun updateManagerLoss(request: UpdateManagerLossRequest): Result<Unit> {
        return lossRemoteDataSource.updateManagerLoss(request)
    }

    override suspend fun deleteManagerLoss(request: DeleteManagerLossRequest): Result<Unit> {
        return lossRemoteDataSource.deleteManagerLoss(request)
    }
}