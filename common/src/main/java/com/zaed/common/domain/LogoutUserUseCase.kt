package com.zaed.common.domain

import com.zaed.common.data.repository.AuthenticationRepository

class LogoutUserUseCase(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke():Result<Unit> = authRepository.logoutCurrentUser()

}