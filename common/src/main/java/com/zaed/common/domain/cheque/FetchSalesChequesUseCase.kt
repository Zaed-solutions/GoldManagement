package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.SalesCheque
import com.zaed.common.data.repository.ChequeRepository
import kotlinx.coroutines.flow.Flow

class FetchSalesChequesUseCase(
    private val chequeRepository: ChequeRepository
) {
    operator fun invoke(): Flow<Result<List<SalesCheque>>> {
        return chequeRepository.fetchSalesCheques()
    }
}