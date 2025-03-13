package com.zaed.common.domain.supplier

import com.zaed.common.data.repository.SupplierRepository

class FetchSuppliersUseCase(
    private val supplierRepo: SupplierRepository
) {
    operator fun invoke() = supplierRepo.fetchSuppliers()
}