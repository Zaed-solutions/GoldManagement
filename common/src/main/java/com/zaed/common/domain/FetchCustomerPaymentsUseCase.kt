package com.zaed.common.domain

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow

class FetchCustomerPaymentsUseCase(
        private val paymentRepository: PaymentRepository
) {
    operator fun invoke(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>{
        return paymentRepository.fetchCustomerPayments(request)
    }
}