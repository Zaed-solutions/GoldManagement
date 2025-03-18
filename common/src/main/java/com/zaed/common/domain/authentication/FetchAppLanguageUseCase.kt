package com.zaed.common.domain.authentication

import com.zaed.common.data.repository.AuthenticationRepository

class FetchAppLanguageUseCase(
    private val authRepo: AuthenticationRepository
) {
    operator fun invoke() = authRepo.fetchAppLanguage()
}