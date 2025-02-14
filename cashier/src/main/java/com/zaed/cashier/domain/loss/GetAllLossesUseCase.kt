package com.zaed.cashier.domain.loss

import com.zaed.cashier.data.model.Loss
import com.zaed.cashier.data.repository.LossRepository
import kotlinx.coroutines.flow.Flow

class GetAllLossesUseCase(
    private val lossRepository: LossRepository
) {
    operator fun invoke(request: GetAllLossesRequest): Flow<Result<List<Loss>>>{
        return lossRepository.getAllLosses(request)
    }
}

data class GetAllLossesRequest(
    val storeId :String =""
)