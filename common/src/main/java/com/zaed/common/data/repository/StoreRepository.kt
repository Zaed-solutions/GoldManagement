package com.zaed.common.data.repository

import com.zaed.common.data.model.Store

interface StoreRepository {
    suspend fun getStores(): Result<List<Store>>
}
