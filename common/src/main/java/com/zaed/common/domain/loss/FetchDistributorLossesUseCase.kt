package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.repository.LossRepository

class FetchDistributorLossesUseCase(
    private val lossRepo: LossRepository
) {
    operator fun invoke(request: FetchDistributorLossesRequest) = lossRepo.fetchDistributorLosses(request)
}