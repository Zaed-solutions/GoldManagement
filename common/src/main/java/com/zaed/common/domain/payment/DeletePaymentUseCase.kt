package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class DeletePaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val customerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: DeletePaymentRequest) : Result<Unit> {
        val result = paymentRepository.deletePayment(request.paymentId)
        if (result.isSuccess) {
            val result2 = customerRepository.deletePayment(request)
            if (result2.isSuccess) {
                return Result.success(Unit)
            } else {
                return Result.failure(result2.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } else {
            return Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
