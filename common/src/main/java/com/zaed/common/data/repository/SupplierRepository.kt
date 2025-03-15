package com.zaed.common.data.repository

import com.zaed.common.data.model.customer.FetchSuppliersByNameRequest
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun fetchSuppliers(): Flow<Result<List<Supplier>>>
    suspend fun addSupplier(request: AddSupplierRequest): Result<Unit>
    suspend fun deleteSupplier(request: DeleteSupplierRequest): Result<Unit>
    suspend fun updateSupplier(request: UpdateSupplierRequest): Result<Unit>
    suspend fun fetchSupplier(request: FetchSupplierRequest): Result<Supplier>
    suspend fun fetchSuppliersByName(request: FetchSuppliersByNameRequest): Result<List<Supplier>>
}