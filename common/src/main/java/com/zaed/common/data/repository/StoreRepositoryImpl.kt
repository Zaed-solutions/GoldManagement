package com.zaed.common.data.repository

import com.zaed.common.data.model.Store

class StoreRepositoryImpl(
    private val remoteDataSource: StoreRemoteDataSource
) : StoreRepository {
    override suspend fun getStores(): Result<List<Store>> {
        return remoteDataSource.getStores()
    }
}