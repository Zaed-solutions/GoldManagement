package com.zaed.common.data.source.remote

import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.data.model.loss.request.AddDistributorLossRequest
import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.model.loss.request.UpdateDistributorLossRequest
import com.zaed.common.data.model.loss.request.UpdateLossRequest
import kotlinx.coroutines.flow.Flow

interface LossRemoteDataSource {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    suspend fun updateLoss(request: UpdateLossRequest): Result<Unit>
    suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit>
    fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<StoreLoss>>>
    fun fetchDistributorLosses(request: FetchDistributorLossesRequest): Flow<Result<List<DistributorLoss>>>
    suspend fun updateDistributorLoss(request: UpdateDistributorLossRequest): Result<Unit>
    suspend fun addDistributorLoss(request: AddDistributorLossRequest): Result<Unit>
    fun fetchStoreLosses(request: FetchStoreLossesRequest): Flow<Result<List<StoreLoss>>>
}
