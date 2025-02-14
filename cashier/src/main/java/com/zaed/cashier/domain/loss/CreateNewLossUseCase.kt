package com.zaed.cashier.domain.loss

import com.zaed.cashier.data.repository.LossRepository

class CreateNewLossUseCase(
    private val lossRepository: LossRepository
) {
    suspend operator fun invoke(request: CreateNewLossRequest): Result<Unit> =
        lossRepository.createNewLoss(request)


}

data class CreateNewLossRequest(
    val value : Double = 0.0,
    val reason : String = "",
)