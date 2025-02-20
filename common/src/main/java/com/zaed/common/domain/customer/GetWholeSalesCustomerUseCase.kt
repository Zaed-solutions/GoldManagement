package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class GetWholeSalesCustomerUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(customerId: String): Result<WholeSaleCustomer> {
        return wholeSalesCustomerRepository.getWholeSaleCustomer(customerId)
    }

}