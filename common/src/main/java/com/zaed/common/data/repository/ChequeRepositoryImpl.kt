package com.zaed.common.data.repository

import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.cheque.request.AddNewManagerChequeRequest
import com.zaed.common.data.model.cheque.request.AddNewSalesChequeRequest
import com.zaed.common.data.model.cheque.request.UpdateChequeStatusRequest
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.source.remote.ChequeRemoteSource
import com.zaed.common.domain.cheque.DeleteManagerChequeRequest
import kotlinx.coroutines.flow.Flow

class ChequeRepositoryImpl(
    private val chequeRemoteSource: ChequeRemoteSource
) : ChequeRepository {
    override suspend fun addNewManagerCheque(request: AddNewManagerChequeRequest): Result<Unit> {
        return chequeRemoteSource.addNewManagerCheque(request)
    }

    override suspend fun addNewSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        return chequeRemoteSource.addNewSalesCheque(request)
    }

    override suspend fun updateManagerCheque(request: AddNewManagerChequeRequest): Result<Unit> {
        return chequeRemoteSource.updateManagerCheque(request)
    }

    override suspend fun updateSalesCheque(request: AddNewSalesChequeRequest): Result<Unit> {
        return chequeRemoteSource.updateSalesCheque(request)
    }

    override suspend fun updateSalesCheckStatus(request: UpdateChequeStatusRequest): Result<Unit> {
        return chequeRemoteSource.updateSalesCheckStatus(request)
    }

    override suspend fun updateManagerCheckStatus(request: UpdateChequeStatusRequest): Result<Unit> {
        return chequeRemoteSource.updateManagerCheckStatus(request)
    }

    override fun fetchManagerCheques(): Flow<Result<List<ManagerCheque>>> {
        return chequeRemoteSource.fetchManagerCheques()
    }

    override fun fetchSalesCheques(): Flow<Result<List<ChequePayment>>> {
        return chequeRemoteSource.fetchSalesCheques()
    }

    override suspend fun fetchAllUnCashedSalesCheque(): Result<List<ChequePayment>> {
        return chequeRemoteSource.fetchAllUnCashedSalesCheque()
    }

    override suspend fun deleteManagerCheque(request: DeleteManagerChequeRequest): Result<Unit> {
        return chequeRemoteSource.deleteManagerCheque(request)
    }
}