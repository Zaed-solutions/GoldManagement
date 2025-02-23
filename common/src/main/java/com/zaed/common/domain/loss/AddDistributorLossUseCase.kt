package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.AddDistributorLossRequest
import com.zaed.common.data.repository.LossRepository

class AddDistributorLossUseCase(
    private val lossRepo: LossRepository
) {
    suspend operator fun invoke(request: AddDistributorLossRequest) = lossRepo.addDistributorLoss(request)
}