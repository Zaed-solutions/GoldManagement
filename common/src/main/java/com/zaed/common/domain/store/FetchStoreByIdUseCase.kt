package com.zaed.common.domain.store

import com.zaed.common.data.model.store.request.FetchStoreByIdRequest
import com.zaed.common.data.repository.StoreRepository

class FetchStoreByIdUseCase(
    private val storeRepo: StoreRepository
) {
    suspend operator fun invoke(request: FetchStoreByIdRequest) = storeRepo.fetchStoreById(request)
}
