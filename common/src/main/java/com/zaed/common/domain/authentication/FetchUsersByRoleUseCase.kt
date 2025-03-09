package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.request.FetchUsersByRoleRequest
import com.zaed.common.data.repository.AuthenticationRepository

class FetchUsersByRoleUseCase(
    private val authRepo: AuthenticationRepository
) {
    operator fun invoke(request: FetchUsersByRoleRequest) = authRepo.fetchUsersByRole(request)
}