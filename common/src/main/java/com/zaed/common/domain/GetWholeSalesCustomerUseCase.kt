package com.zaed.common.domain

import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class GetWholeSalesCustomerUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(customerId: String): Result<WholeSaleCustomer> {
        return wholeSalesCustomerRepository.getWholeSaleCustomer(customerId)
    }

}