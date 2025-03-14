package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.repository.ChequeRepository

class UpdateManagerChequeUseCase(
    private val chequeRepository: ChequeRepository
) {
    suspend operator fun invoke(request: AddNewManagerChequeRequest): Result<Unit> {
        return chequeRepository.updateManagerCheque(request)
    }
}
class DeleteManagerChequeUseCase(
    private val chequeRepository: ChequeRepository
) {
    suspend operator fun invoke(request: DeleteManagerChequeRequest): Result<Unit> {
        return chequeRepository.deleteManagerCheque(request)
    }
}

data class DeleteManagerChequeRequest(
    val supplierId: String,
    val managerCheque: ManagerCheque
)