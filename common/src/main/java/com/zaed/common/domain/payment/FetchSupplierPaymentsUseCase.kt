package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.FetchSupplierPaymentsRequest
import com.zaed.common.data.repository.PaymentRepository

class FetchSupplierPaymentsUseCase(
    private val paymentRepo: PaymentRepository
) {
    operator fun invoke(request: FetchSupplierPaymentsRequest) = paymentRepo.fetchSupplierPayments(request)
}