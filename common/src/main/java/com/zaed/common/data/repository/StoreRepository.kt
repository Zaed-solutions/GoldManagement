package com.zaed.common.data.repository

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest

interface StoreRepository {
    suspend fun getStores(): Result<List<Store>>
    suspend fun addStore(request: AddStoreRequest): Result<Unit>
    suspend fun updateStore(request: UpdateStoreRequest): Result<Unit>
    suspend fun deleteStore(request: DeleteStoreRequest): Result<Unit>
    suspend fun fetchStoreById(request: FetchStoreByIdRequest): Result<Store>
}
