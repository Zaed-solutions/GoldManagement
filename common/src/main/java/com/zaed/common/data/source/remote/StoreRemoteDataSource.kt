package com.zaed.common.data.source.remote

import com.zaed.common.data.model.Store

interface StoreRemoteDataSource {
    suspend fun getStores(): Result<List<Store>>
}
