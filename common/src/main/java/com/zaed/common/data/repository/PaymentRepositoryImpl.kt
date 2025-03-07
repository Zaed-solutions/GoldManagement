package com.zaed.common.data.repository

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.source.remote.PaymentRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PaymentRepositoryImpl(
    private val paymentDataSource: PaymentRemoteDataSource
) : PaymentRepository {
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<String> {
        return paymentDataSource.addPayment(request)
    }

    override suspend fun fetchMoneyPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>> {
        return paymentDataSource.fetchMoneyPaymentsByIds(request)
    }

    override suspend fun fetchGoldPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<GoldPayment>> {
        return paymentDataSource.fetchGoldPaymentsByIds(request)
    }


    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        return paymentDataSource.deletePayment(request)
    }

    override suspend fun editPayment(request: EditPaymentRequest): Result<Unit> {
        return paymentDataSource.editPayment(request)
    }

    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> {
        return paymentDataSource.fetchCustomerPayments(request)
    }
}

