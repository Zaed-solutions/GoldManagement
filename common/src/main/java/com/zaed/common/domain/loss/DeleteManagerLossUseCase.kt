package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.DeleteManagerLossRequest
import com.zaed.common.data.repository.LossRepository

class DeleteManagerLossUseCase(
    private val lossRepo: LossRepository
){
    suspend operator fun invoke(request: DeleteManagerLossRequest) = lossRepo.deleteManagerLoss(request)
}