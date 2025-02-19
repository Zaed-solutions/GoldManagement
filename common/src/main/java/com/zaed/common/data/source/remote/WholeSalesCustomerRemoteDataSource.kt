package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import kotlinx.coroutines.flow.Flow

interface WholeSalesCustomerRemoteDataSource {
    fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>>
   suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest) :Result<Unit>
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
}
