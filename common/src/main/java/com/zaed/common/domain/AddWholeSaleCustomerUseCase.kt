package com.zaed.common.domain

import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class AddWholeSaleCustomerUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: AddWholeSaleCustomerRequest) :Result<Unit> {
        return wholeSalesCustomerRepository.addWholeSaleCustomer(request)
    }
}


