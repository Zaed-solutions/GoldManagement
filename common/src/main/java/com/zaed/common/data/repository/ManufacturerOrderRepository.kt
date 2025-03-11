package com.zaed.common.data.repository

import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.data.model.manufacturerorder.request.AddManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.DeleteManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.UpdateManufacturerOrderRequest
import kotlinx.coroutines.flow.Flow

interface ManufacturerOrderRepository {
    fun fetchManufacturerOrders(): Flow<Result<List<ManufacturerOrder>>>
    suspend fun addManufacturerOrder(request: AddManufacturerOrderRequest): Result<Unit>
    suspend fun updateManufacturerOrder(request: UpdateManufacturerOrderRequest): Result<Unit>
    suspend fun deleteManufacturerOrder(request: DeleteManufacturerOrderRequest): Result<Unit>
}