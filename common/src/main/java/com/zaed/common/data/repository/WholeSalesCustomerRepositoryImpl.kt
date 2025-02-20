package com.zaed.common.data.repository

import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.source.remote.WholeSalesCustomerRemoteDataSource
import com.zaed.common.domain.payment.UpdateCustomerDebtRequest
import kotlinx.coroutines.flow.Flow

class WholeSalesCustomerRepositoryImpl(
    private val wholeSalesCustomerRemoteDataSource: WholeSalesCustomerRemoteDataSource
) : WholeSalesCustomerRepository {
    override fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>> =
        wholeSalesCustomerRemoteDataSource.getWholeSalesCustomers()

    override suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit> {
       return wholeSalesCustomerRemoteDataSource.addWholeSaleCustomer(request)
    }

    override suspend fun getWholeSaleCustomer(customerId: String): Result<WholeSaleCustomer> {
        return wholeSalesCustomerRemoteDataSource.getWholeSaleCustomer(customerId)
    }

    override suspend fun updateCustomerDebt(updateCustomerDebtRequest: UpdateCustomerDebtRequest): Result<Unit> {
        return wholeSalesCustomerRemoteDataSource.updateCustomerDebt(updateCustomerDebtRequest)
    }

    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        return wholeSalesCustomerRemoteDataSource.deletePayment(request)
    }

    override suspend fun addNewPayment(request: AddNewPaymentRequest): Result<Unit> {
        return wholeSalesCustomerRemoteDataSource.addNewPayment(request)
    }
}