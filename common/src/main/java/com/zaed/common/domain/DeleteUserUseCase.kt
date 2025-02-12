package com.zaed.common.domain

import com.zaed.common.data.model.request.DeleteUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class DeleteUserUseCase(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(request: DeleteUserRequest) = authRepo.deleteUser(request)
}