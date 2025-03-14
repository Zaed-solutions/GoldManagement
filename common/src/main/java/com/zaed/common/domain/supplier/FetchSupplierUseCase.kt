package com.zaed.common.domain.supplier

import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.repository.SupplierRepository

class FetchSupplierUseCase(
    private val supplierRepo: SupplierRepository
) {
    suspend operator fun invoke(request: FetchSupplierRequest) = supplierRepo.fetchSupplier(request)
}