package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.repository.ChequeRepository
import kotlinx.coroutines.flow.Flow

class FetchManagerChequesUseCase(
    private val chequeRepository: ChequeRepository
) {
    operator fun invoke(): Flow<Result<List<ManagerCheque>>> {
        return chequeRepository.fetchManagerCheques()
    }
}