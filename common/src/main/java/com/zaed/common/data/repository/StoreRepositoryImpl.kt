package com.zaed.common.data.repository

import com.zaed.common.data.model.Store
import com.zaed.common.data.source.remote.StoreRemoteDataSource

class StoreRepositoryImpl(
    private val remoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    override suspend fun getStores(): Result<List<Store>> {
        return remoteDataSource.getStores()
    }
}