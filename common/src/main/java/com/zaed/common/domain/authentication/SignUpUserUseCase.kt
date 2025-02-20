package com.zaed.common.domain.authentication

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.request.SignUpUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class SignUpUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(request: SignUpUserRequest): Result<User> = authenticationRepository.signUpUser(request)
}