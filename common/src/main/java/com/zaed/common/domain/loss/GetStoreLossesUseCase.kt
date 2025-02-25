package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.repository.LossRepository
import kotlinx.coroutines.flow.Flow

class GetStoreLossesUseCase(
    private val lossRepository: LossRepository
) {
    operator fun invoke(request: GetStoreLossesRequest): Flow<Result<List<StoreLoss>>>{
        return lossRepository.getStoreLosses(request)
    }

}
