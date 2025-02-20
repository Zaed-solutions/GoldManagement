package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.request.DeleteUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class DeleteUserUseCase(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(request: DeleteUserRequest) = authRepo.deleteUser(request)
}