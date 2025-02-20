package com.zaed.common.domain.store

import com.zaed.common.data.model.Store
import com.zaed.common.data.repository.StoreRepository

class GetStoresUseCase(
    private val repository: StoreRepository
){
    suspend operator fun invoke(): Result<List<Store>> {
        return repository.getStores()
    }
}