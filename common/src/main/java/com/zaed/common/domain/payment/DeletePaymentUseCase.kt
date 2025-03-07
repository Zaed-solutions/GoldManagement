package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class DeletePaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val customerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: DeletePaymentRequest) : Result<Unit> {
        return paymentRepository.deletePayment(request)
    }
}
