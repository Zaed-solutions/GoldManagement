package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddWholeSaleCustomerUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: AddWholeSaleCustomerRequest) :Result<Unit> {
        return wholeSalesCustomerRepository.addWholeSaleCustomer(request)
    }
}


