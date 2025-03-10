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
import kotlinx.coroutines.flow.Flow

interface LossRepository {
    suspend fun createNewLoss(request: CreateNewLossRequest): Result<Unit>
    suspend fun updateLoss(request: UpdateLossRequest): Result<Unit>
    suspend fun deleteLoss(request: DeleteLossRequest): Result<Unit>
    fun getStoreLosses(request: GetStoreLossesRequest): Flow<Result<List<StoreLoss>>>
    fun fetchDistributorLosses(request: FetchDistributorLossesRequest): Flow<Result<List<DistributorLoss>>>
    suspend fun addDistributorLoss(request: AddDistributorLossRequest): Result<Unit>
    suspend fun updateDistributorLoss(request: UpdateDistributorLossRequest): Result<Unit>
    fun fetchStoreLosses(request: FetchStoreLossesRequest): Flow<Result<List<StoreLoss>>>
    fun fetchManagerLosses(): Flow<Result<List<ManagerLoss>>>
    suspend fun addManagerLoss(request: AddManagerLossRequest): Result<Unit>
    suspend fun updateManagerLoss(request: UpdateManagerLossRequest): Result<Unit>
    suspend fun deleteManagerLoss(request: DeleteManagerLossRequest): Result<Unit>
}
