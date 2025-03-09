package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.repository.PaymentRepository

class EditPaymentUseCase(
    private val paymentRepository: PaymentRepository,
){
    suspend operator fun invoke(request: EditPaymentRequest): Result<Unit>  {
        return paymentRepository.editPayment(request)
    }
}
