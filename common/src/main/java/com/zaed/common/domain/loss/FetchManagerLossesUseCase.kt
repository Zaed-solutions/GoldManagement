package com.zaed.common.domain.loss

import com.zaed.common.data.repository.LossRepository

class FetchManagerLossesUseCase(
    private val lossRepo: LossRepository
) {
    operator fun invoke() = lossRepo.fetchManagerLosses()
}

