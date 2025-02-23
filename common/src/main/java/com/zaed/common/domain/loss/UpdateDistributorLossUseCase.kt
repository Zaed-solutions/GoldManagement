package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.UpdateDistributorLossRequest
import com.zaed.common.data.repository.LossRepository

class UpdateDistributorLossUseCase(
    private val lossRepo: LossRepository
) {
    suspend operator fun invoke(request: UpdateDistributorLossRequest) = lossRepo.updateDistributorLoss(request)
}