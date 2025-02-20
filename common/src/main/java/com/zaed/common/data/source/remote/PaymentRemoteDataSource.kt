package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.AddNewPaymentRequest
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRemoteDataSource {
    suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit>
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>

}