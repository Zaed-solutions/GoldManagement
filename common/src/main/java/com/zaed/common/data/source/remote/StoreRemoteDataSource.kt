package com.zaed.common.data.source.remote

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest

interface StoreRemoteDataSource {
    suspend fun getStores(): Result<List<Store>>
    suspend fun addStore(request: AddStoreRequest): Result<Unit>
    suspend fun updateStore(request: UpdateStoreRequest): Result<Unit>
    suspend fun deleteStore(request: DeleteStoreRequest): Result<Unit>
}
