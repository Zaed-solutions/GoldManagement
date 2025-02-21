package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class FetchWholesaleCustomersByNameUseCase(
    private val customerRepo: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: FetchWholesaleCustomersByNameRequest) = customerRepo.fetchWholesaleCustomersByName(request)
}