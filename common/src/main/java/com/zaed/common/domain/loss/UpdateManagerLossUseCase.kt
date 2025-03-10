package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.UpdateManagerLossRequest
import com.zaed.common.data.repository.LossRepository

class UpdateManagerLossUseCase(
    private val lossRepo: LossRepository
){
    suspend operator fun invoke(request: UpdateManagerLossRequest) = lossRepo.updateManagerLoss(request)
}