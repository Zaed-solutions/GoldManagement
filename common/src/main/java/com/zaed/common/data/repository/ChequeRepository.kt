package com.zaed.common.data.repository

import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.model.cheque.request.AddNewSalesChequeRequest
import com.zaed.common.data.model.cheque.request.UpdateChequeStatusRequest
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.domain.cheque.DeleteManagerChequeRequest
import kotlinx.coroutines.flow.Flow

interface ChequeRepository {
    suspend fun addNewManagerCheque(request: AddNewManagerChequeRequest): Result<Unit>
    suspend fun addNewSalesCheque(request: AddNewSalesChequeRequest): Result<Unit>
    suspend fun updateManagerCheque(request: AddNewManagerChequeRequest): Result<Unit>
    suspend fun updateSalesCheque(request: AddNewSalesChequeRequest): Result<Unit>
    suspend fun updateSalesCheckStatus(request: UpdateChequeStatusRequest): Result<Unit>
    suspend fun updateManagerCheckStatus(request: UpdateChequeStatusRequest): Result<Unit>
    fun fetchManagerCheques(): Flow<Result<List<ManagerCheque>>>
    fun fetchSalesCheques(): Flow<Result<List<ChequePayment>>>
    suspend fun deleteManagerCheque(request: DeleteManagerChequeRequest): Result<Unit>
}