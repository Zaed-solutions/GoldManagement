package com.zaed.common.domain.store

import com.zaed.common.data.model.store.request.AddStoreRequest
import com.zaed.common.data.repository.StoreRepository

class AddStoreUseCase(
    private val storeRepo: StoreRepository
) {
    suspend operator fun invoke(request: AddStoreRequest) = storeRepo.addStore(request)
}