package com.zaed.common.domain

import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class SignUpUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(request: SignUpUserRequest): Result<User> = authenticationRepository.signUpUser(request)
}