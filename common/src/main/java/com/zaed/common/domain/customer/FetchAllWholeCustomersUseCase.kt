package com.zaed.common.domain.customer

import com.zaed.common.data.repository.WholeSalesCustomerRepository

class FetchAllWholeCustomersUseCase(
    private val customerRepo: WholeSalesCustomerRepository
) {
    operator fun invoke() = customerRepo.fetchAllWholeCustomers()
}