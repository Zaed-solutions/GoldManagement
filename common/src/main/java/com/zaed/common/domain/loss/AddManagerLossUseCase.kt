package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.AddManagerLossRequest
import com.zaed.common.data.repository.LossRepository

class AddManagerLossUseCase(
    private val lossRepo: LossRepository
){
    suspend operator fun invoke(request: AddManagerLossRequest) = lossRepo.addManagerLoss(request)
}