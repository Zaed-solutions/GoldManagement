package com.zaed.common.data.repository

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.source.remote.WholeSalesCustomerRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WholeSalesCustomerRepositoryImpl(
    private val wholeSalesCustomerRemoteDataSource: WholeSalesCustomerRemoteDataSource
) : WholeSalesCustomerRepository {
    override fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>> =
        wholeSalesCustomerRemoteDataSource.getWholeSalesCustomers()

    override suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit> {
       return wholeSalesCustomerRemoteDataSource.addWholeSaleCustomer(request)
    }

    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> {
        return wholeSalesCustomerRemoteDataSource.fetchCustomerPayments(request)
    }
}