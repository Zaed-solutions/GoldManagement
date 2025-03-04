package com.zaed.common.domain.cheque

import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.repository.ChequeRepository

class AddManagerChequeUseCase(
private val chequeRepository: ChequeRepository
){
    suspend operator fun invoke(request: AddNewManagerChequeRequest):Result<Unit>{
        return chequeRepository.addNewManagerCheque(request)
    }
}