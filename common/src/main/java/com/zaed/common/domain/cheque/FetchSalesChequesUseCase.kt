package com.zaed.common.domain.cheque

import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.repository.ChequeRepository
import kotlinx.coroutines.flow.Flow

class FetchSalesChequesUseCase(
    private val chequeRepository: ChequeRepository
) {
    operator fun invoke(): Flow<Result<List<ChequePayment>>> {
        return chequeRepository.fetchSalesCheques()
    }
}
class FetchAllUnCashedSalesChequeUseCase(
    private val chequeRepository: ChequeRepository
) {
    suspend operator fun invoke(): Result<List<ChequePayment>> {
        return chequeRepository.fetchAllUnCashedSalesCheque()
    }
}