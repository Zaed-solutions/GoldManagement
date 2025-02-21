package com.zaed.common.domain.customer

import com.zaed.common.data.repository.WholeSalesCustomerRepository

class DeleteWholeSaleCustomerUseCase(
    private val customerRepository: WholeSalesCustomerRepository
) {
//    suspend operator fun invoke(customerId: String):Result<Unit> {
//        return customerRepository.deleteCustomer(customerId)
//    }
}