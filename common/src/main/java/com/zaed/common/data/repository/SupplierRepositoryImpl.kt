package com.zaed.common.data.repository

import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.data.source.remote.SupplierRemoteSource
import kotlinx.coroutines.flow.Flow

class SupplierRepositoryImpl(
    private val remoteSource: SupplierRemoteSource
) : SupplierRepository {
    override fun fetchSuppliers() = remoteSource.fetchSuppliers()

    override suspend fun addSupplier(request: AddSupplierRequest) = remoteSource.addSupplier(request)

    override suspend fun deleteSupplier(request: DeleteSupplierRequest) = remoteSource.deleteSupplier(request)

    override suspend fun updateSupplier(request: UpdateSupplierRequest) = remoteSource.updateSupplier(request)
    override suspend fun fetchSupplier(request: FetchSupplierRequest) = remoteSource.fetchSupplier(request)
}