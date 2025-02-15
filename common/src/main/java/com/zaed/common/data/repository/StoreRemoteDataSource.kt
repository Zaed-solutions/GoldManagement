package com.zaed.common.data.repository

import com.zaed.common.data.model.Store

interface StoreRemoteDataSource {
    suspend fun getStores(): Result<List<Store>>
}
