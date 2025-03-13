package com.zaed.common.domain.supplier

import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.data.repository.SupplierRepository

class UpdateSupplierUseCase(
    private val supplierRepo: SupplierRepository
) {
    suspend operator fun invoke(request: UpdateSupplierRequest) = supplierRepo.updateSupplier(request)
}