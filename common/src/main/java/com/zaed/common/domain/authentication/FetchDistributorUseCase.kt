package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.request.FetchDistributorRequest
import com.zaed.common.data.repository.AuthenticationRepository

class FetchDistributorUseCase(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(request: FetchDistributorRequest) = authRepo.fetchDistributor(request)
}