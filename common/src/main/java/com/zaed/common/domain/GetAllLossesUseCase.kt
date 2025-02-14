package com.zaed.common.domain

import com.zaed.common.data.model.Loss
import com.zaed.common.data.model.request.GetAllLossesRequest
import com.zaed.common.data.repository.LossRepository
import kotlinx.coroutines.flow.Flow

class GetAllLossesUseCase(
    private val lossRepository: LossRepository
) {
    operator fun invoke(request: GetAllLossesRequest): Flow<Result<List<Loss>>>{
        return lossRepository.getAllLosses(request)
    }
}
