package com.zaed.common.data.repository

import com.zaed.common.data.model.customer.FetchSuppliersByNameRequest
import com.zaed.common.data.model.supplier.request.AddSupplierRequest
import com.zaed.common.data.model.supplier.request.DeleteSupplierRequest
import com.zaed.common.data.model.supplier.request.FetchSupplierRequest
import com.zaed.common.data.model.supplier.request.UpdateSupplierRequest
import com.zaed.common.data.source.remote.SupplierRemoteSource

class SupplierRepositoryImpl(
    private val remoteSource: SupplierRemoteSource
) : SupplierRepository {
    override fun fetchSuppliers() = remoteSource.fetchSuppliers()

    override suspend fun addSupplier(request: AddSupplierRequest) =
        remoteSource.addSupplier(request)

    override suspend fun deleteSupplier(request: DeleteSupplierRequest) =
        remoteSource.deleteSupplier(request)

    override suspend fun updateSupplier(request: UpdateSupplierRequest) =
        remoteSource.updateSupplier(request)

    override suspend fun fetchSupplier(request: FetchSupplierRequest) =
        remoteSource.fetchSupplier(request)

    override suspend fun fetchSuppliersByName(request: FetchSuppliersByNameRequest) =
        remoteSource.fetchSuppliersByName(request)
}