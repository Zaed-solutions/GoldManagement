package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.CreateNewLossRequest
import com.zaed.common.data.repository.LossRepository

class CreateNewLossUseCase(
    private val lossRepository: LossRepository
) {
    suspend operator fun invoke(request: CreateNewLossRequest): Result<Unit> =
        lossRepository.createNewLoss(request)


}

