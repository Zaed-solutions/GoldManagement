package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddNewPaymentUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(request: AddNewPaymentRequest) = paymentRepository.addPayment(request)
}

data class UpdateCustomerDebtRequest(
    val customerId: String,
    val difference : Double,
)

