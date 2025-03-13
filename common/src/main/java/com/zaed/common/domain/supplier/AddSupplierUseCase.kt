package com.zaed.common.domain.supplier

import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.repository.SupplierRepository

class AddSupplierUseCase(
    private val supplierRepo: SupplierRepository
) {
    suspend operator fun invoke(request: AddSupplierRequest) = supplierRepo.addSupplier(request)
}