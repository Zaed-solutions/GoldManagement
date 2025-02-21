package com.zaed.common.data.repository

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.source.remote.PaymentRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PaymentRepositoryImpl(
    private val paymentDataSource: PaymentRemoteDataSource
) : PaymentRepository {
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit> {
        return paymentDataSource.addPayment(request)
    }

    override suspend fun fetchPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>> {
        return paymentDataSource.fetchPaymentsByIds(request)
    }

    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> {
        return paymentDataSource.fetchCustomerPayments(request)
    }
}

