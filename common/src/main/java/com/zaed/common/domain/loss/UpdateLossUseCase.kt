package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.UpdateLossRequest
import com.zaed.common.data.repository.LossRepository

class UpdateLossUseCase(
    private val lossRepo: LossRepository
) {
    suspend operator fun invoke(request: UpdateLossRequest) = lossRepo.updateLoss(request)
}