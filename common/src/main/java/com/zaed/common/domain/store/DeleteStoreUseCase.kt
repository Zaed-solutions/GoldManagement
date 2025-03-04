package com.zaed.common.domain.store

import com.zaed.common.data.model.store.request.DeleteStoreRequest
import com.zaed.common.data.repository.StoreRepository

class DeleteStoreUseCase(
    private val storeRepo: StoreRepository
) {
    suspend operator fun invoke(request: DeleteStoreRequest) = storeRepo.deleteStore(request)
}