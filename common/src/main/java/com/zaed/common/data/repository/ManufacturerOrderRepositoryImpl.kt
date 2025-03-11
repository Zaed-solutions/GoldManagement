package com.zaed.common.data.repository

import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.data.model.manufacturerorder.request.AddManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.DeleteManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.UpdateManufacturerOrderRequest
import com.zaed.common.data.source.remote.ManufacturerOrderRemoteSource
import kotlinx.coroutines.flow.Flow

class ManufacturerOrderRepositoryImpl(
    private val remoteSource: ManufacturerOrderRemoteSource
) : ManufacturerOrderRepository {
    override fun fetchManufacturerOrders(): Flow<Result<List<ManufacturerOrder>>> {
        return remoteSource.fetchManufacturerOrders()
    }

    override suspend fun addManufacturerOrder(request: AddManufacturerOrderRequest): Result<Unit> {
        return remoteSource.addManufacturerOrder(request)
    }

    override suspend fun updateManufacturerOrder(request: UpdateManufacturerOrderRequest): Result<Unit> {
        return remoteSource.updateManufacturerOrder(request)
    }

    override suspend fun deleteManufacturerOrder(request: DeleteManufacturerOrderRequest): Result<Unit> {
        return remoteSource.deleteManufacturerOrder(request)
    }
}