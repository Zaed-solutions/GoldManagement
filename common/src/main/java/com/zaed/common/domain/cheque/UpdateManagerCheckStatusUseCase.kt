package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.request.UpdateChequeStatusRequest
import com.zaed.common.data.repository.ChequeRepository

class UpdateManagerCheckStatusUseCase(
    private val chequeRepository: ChequeRepository
) {
    suspend operator fun invoke(request: UpdateChequeStatusRequest): Result<Unit> {
        return chequeRepository.updateManagerCheckStatus(request)
    }
}