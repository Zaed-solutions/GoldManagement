package com.zaed.common.domain

import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.repository.AuthenticationRepository

class LoginUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(request: LoginUserRequest):Result<User> =
        authenticationRepository.loginUser(request)
}

