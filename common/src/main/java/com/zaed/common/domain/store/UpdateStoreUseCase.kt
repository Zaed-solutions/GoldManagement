package com.zaed.common.domain.store

import com.zaed.common.data.model.store.request.UpdateStoreRequest
import com.zaed.common.data.repository.StoreRepository

class UpdateStoreUseCase(
    private val storeRepo: StoreRepository
) {
    suspend operator fun invoke(request: UpdateStoreRequest) = storeRepo.updateStore(request)
}