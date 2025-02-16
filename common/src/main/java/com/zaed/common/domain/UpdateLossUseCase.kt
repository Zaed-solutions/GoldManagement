package com.zaed.common.domain

import com.zaed.common.data.model.request.UpdateLossRequest
import com.zaed.common.data.repository.LossRepository

class UpdateLossUseCase(
    private val lossRepo: LossRepository
) {
    suspend operator fun invoke(request: UpdateLossRequest) = lossRepo.updateLoss(request)
}