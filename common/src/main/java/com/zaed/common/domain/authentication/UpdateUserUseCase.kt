package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.request.UpdateUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class UpdateUserUseCase(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(request: UpdateUserRequest) = authRepo.updateUser(request)
}