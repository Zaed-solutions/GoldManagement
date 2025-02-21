package com.zaed.common.data.repository

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
    suspend fun addPayment(request: AddNewPaymentRequest): Result<String>
    suspend fun deletePayment(id: String): Result<Unit>
    suspend fun editPayment(request: EditPaymentRequest): Result<Unit>
}