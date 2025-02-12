package com.zaed.common.domain

import com.zaed.common.data.model.User
import com.zaed.common.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserLoggedInUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Result<User>> = authenticationRepository.getCurrentUser()
}

