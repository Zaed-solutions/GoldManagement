package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class EditPaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val customerRepository:WholeSalesCustomerRepository
){
    suspend operator fun invoke(request: EditPaymentRequest): Result<Unit>  {
        val result = paymentRepository.editPayment(request)
        if (result.isSuccess) {
            val result2 = customerRepository.updateCustomerDebt(
                UpdateCustomerDebtRequest(
                    customerId = request.payment.customerId,
                    paymentId = request.payment.id,
                    amount = request.payment.amount
                )
            )
            if (result2.isSuccess) {
                return Result.success(Unit)
            } else {
                return Result.failure(result2.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }else{
            return Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}
