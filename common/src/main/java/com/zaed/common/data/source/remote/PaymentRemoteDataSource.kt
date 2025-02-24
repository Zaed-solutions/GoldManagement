package com.zaed.common.data.source.remote

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import kotlinx.coroutines.flow.Flow

interface PaymentRemoteDataSource {
    suspend fun addPayment(request: AddNewPaymentRequest): Result<String>
    fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<MoneyPayment>>>
    suspend fun fetchMoneyPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<MoneyPayment>>
    suspend fun deletePayment(id: String): Result<Unit>
    suspend fun editPayment(request: EditPaymentRequest): Result<Unit>
    suspend fun fetchGoldPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<GoldPayment>>
}