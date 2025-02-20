package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.DeleteLossRequest
import com.zaed.common.data.repository.LossRepository

class DeleteLossUseCase(
    private val lossRepo: LossRepository
) {
    suspend operator fun invoke(request: DeleteLossRequest) = lossRepo.deleteLoss(request)
}