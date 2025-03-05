package com.zaed.common.data.repository

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.data.source.remote.StoreRemoteDataSource
import kotlinx.coroutines.flow.Flow

class StoreRepositoryImpl(
    private val remoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    override fun getStores(): Flow<Result<List<Store>>> {
        return remoteDataSource.getStores()
    }

    override suspend fun addStore(request: AddStoreRequest): Result<Unit> {
        return remoteDataSource.addStore(request)
    }

    override suspend fun updateStore(request: UpdateStoreRequest): Result<Unit> {
        return remoteDataSource.updateStore(request)
    }

    override suspend fun deleteStore(request: DeleteStoreRequest): Result<Unit> {
        return remoteDataSource.deleteStore(request)
    }

    override suspend fun fetchStoreById(request: FetchStoreByIdRequest): Result<Store> {
        return remoteDataSource.fetchStoreById(request)
    }
}