package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.request.AddNewSalesChequeRequest
import com.zaed.common.data.repository.ChequeRepository

class UpdateSalesChequeUseCase(
    private val chequeRepository: ChequeRepository
) {
    suspend operator fun invoke(request: AddNewSalesChequeRequest): Result<Unit> {
        return chequeRepository.updateSalesCheque(request)
    }
}