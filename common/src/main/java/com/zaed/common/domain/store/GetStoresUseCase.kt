package com.zaed.common.domain.store

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.repository.StoreRepository

class GetStoresUseCase(
    private val repository: StoreRepository
){
    operator fun invoke() =  repository.getStores()
}