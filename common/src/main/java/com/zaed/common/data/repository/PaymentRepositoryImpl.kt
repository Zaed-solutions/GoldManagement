package com.zaed.common.data.repository

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.AddNewPaymentRequest
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.source.remote.PaymentRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PaymentRepositoryImpl(
    private val paymentDataSource: PaymentRemoteDataSource
) : PaymentRepository {
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<Unit> {
        return paymentDataSource.addPayment(request)
    }
    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> {
        return paymentDataSource.fetchCustomerPayments(request)
    }
}

