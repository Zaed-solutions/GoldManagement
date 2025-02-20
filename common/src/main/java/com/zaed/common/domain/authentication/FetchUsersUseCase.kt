package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class FetchUsersUseCase(
    private val authRepo: AuthenticationRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> = authRepo.fetchUsers()
}