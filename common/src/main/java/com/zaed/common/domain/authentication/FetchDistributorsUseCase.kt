package com.zaed.common.domain.authentication

import com.zaed.common.data.repository.AuthenticationRepository

class FetchDistributorsUseCase(
    private val authRepo: AuthenticationRepository
) {
    operator fun invoke() = authRepo.fetchDistributors()
}