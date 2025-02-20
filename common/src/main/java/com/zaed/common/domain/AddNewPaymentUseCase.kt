package com.zaed.common.domain

import com.zaed.common.data.model.request.AddNewPaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddNewPaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository,
) {
    suspend operator fun invoke(request: AddNewPaymentRequest): Result<Unit> {
        val result = paymentRepository.addPayment(request)
        if (result.isSuccess) {
            val result2 = wholeSalesCustomerRepository.updateCustomerDebt(
                UpdateCustomerDebtRequest(
                    customerId = request.customerId,
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

data class UpdateCustomerDebtRequest(
    val customerId: String,
    val amount: Double
)

