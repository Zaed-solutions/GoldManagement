package com.zaed.common.domain

import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class SignUpUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(request: SignUpUserRequest): Flow<Result<User>> = authenticationRepository.signUpUser(request)
}