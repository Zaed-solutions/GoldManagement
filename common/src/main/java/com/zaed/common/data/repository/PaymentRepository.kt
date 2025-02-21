package com.zaed.common.data.repository

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
    suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit>
    suspend fun fetchPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>>
}