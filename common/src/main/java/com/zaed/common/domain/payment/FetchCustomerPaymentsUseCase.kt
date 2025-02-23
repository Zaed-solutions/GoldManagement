package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow

class FetchCustomerPaymentsUseCase(
        private val paymentRepository: PaymentRepository
) {
    operator fun invoke(request: FetchCustomerPaymentsRequest): Flow<Result<List<MoneyPayment>>>{
        return paymentRepository.fetchCustomerPayments(request)
    }
}