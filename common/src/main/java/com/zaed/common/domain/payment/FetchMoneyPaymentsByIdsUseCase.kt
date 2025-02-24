package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.repository.PaymentRepository

class FetchMoneyPaymentsByIdsUseCase(
    private val paymentRepo: PaymentRepository
) {
    suspend operator fun invoke(request: FetchPaymentsByIdsRequest) = paymentRepo.fetchMoneyPaymentsByIds(request)
}