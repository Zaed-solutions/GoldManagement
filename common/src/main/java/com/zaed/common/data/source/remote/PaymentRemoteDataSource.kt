package com.zaed.common.data.source.remote

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRemoteDataSource {
    suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit>
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
    suspend fun fetchPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>>
}