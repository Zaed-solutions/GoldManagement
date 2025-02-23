package com.zaed.common.domain.sale

import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository,
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: AddWholesaleProductSaleRequest): Result<String> {
        val result = saleRepo.addWholesaleProductSale(request)
        if (result.isSuccess) {
            val result2 = request.moneyPayments.filter { it.type == PaymentType.FUTURES }.map { payment ->
                wholeSalesCustomerRepository.addNewPayment(
                    request= AddNewPaymentRequest(
                        customerId = request.sale.customerId,
                        moneyPayment = payment.copy(amount = payment.amount.unaryMinus())
                    )
                ).isSuccess
            }
            if (result2.all { true }) {
                return Result.success(result.getOrNull()?:"")
            } else {
                return Result.failure(Exception("Failed to add payments"))
            }
        }else{
            return Result.failure(Exception("Failed to add sale"))
        }

    }
}