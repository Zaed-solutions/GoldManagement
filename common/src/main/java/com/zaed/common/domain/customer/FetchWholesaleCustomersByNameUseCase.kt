package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.FetchSuppliersByNameRequest
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.repository.SupplierRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class FetchWholesaleCustomersByNameUseCase(
    private val customerRepo: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: FetchWholesaleCustomersByNameRequest) = customerRepo.fetchWholesaleCustomersByName(request)
}

class FetchSuppliersByNameUseCase(
    private val supplierRepository: SupplierRepository
) {
    suspend operator fun invoke(request: FetchSuppliersByNameRequest) = supplierRepository.fetchSuppliersByName(request)
}