package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.request.FetchStoreLossesRequest
import com.zaed.common.data.repository.LossRepository

class FetchStoreLossesUseCase(
    private val lossRepo: LossRepository
) {
    operator fun invoke(request: FetchStoreLossesRequest) = lossRepo.fetchStoreLosses(request)
}