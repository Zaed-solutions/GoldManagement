package com.zaed.common.data.repository

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.payment.request.FetchSupplierPaymentsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>
    suspend fun fetchMoneyPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>>
    suspend fun addPayment(request: AddNewPaymentRequest): Result<String>
    suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit>
    suspend fun editPayment(request: EditPaymentRequest): Result<Unit>
    suspend fun fetchGoldPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<GoldPayment>>
    fun fetchSupplierPayments(request: FetchSupplierPaymentsRequest): Flow<Result<List<Payment>>>
}