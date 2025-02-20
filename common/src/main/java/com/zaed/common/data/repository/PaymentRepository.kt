package com.zaed.common.data.repository

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.AddNewPaymentRequest
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
    suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit>
}