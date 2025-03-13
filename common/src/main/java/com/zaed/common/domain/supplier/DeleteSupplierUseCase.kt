package com.zaed.common.domain.supplier

import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.repository.SupplierRepository

class DeleteSupplierUseCase(
    private val supplierRepo: SupplierRepository
) {
    suspend operator fun invoke(request: DeleteSupplierRequest) = supplierRepo.deleteSupplier(request)
}