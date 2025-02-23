package com.zaed.common.domain.payment

import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddNewPaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository,
) {
    suspend operator fun invoke(request: AddNewPaymentRequest): Result<Unit> {
        val result = paymentRepository.addPayment(request)
        if (result.isSuccess) {
            val result2 = wholeSalesCustomerRepository.addNewPayment(
                request.copy(
                    payment = request.payment.copy(id = result.getOrNull() ?: "")
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
    val difference : Double,
)

