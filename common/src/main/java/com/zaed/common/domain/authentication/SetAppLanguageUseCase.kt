package com.zaed.common.domain.authentication

import com.zaed.common.data.repository.AuthenticationRepository
import com.zaed.common.ui.util.AppLanguage

class SetAppLanguageUseCase(
    private val authRepo: AuthenticationRepository
) {
    suspend operator fun invoke(appLanguage: AppLanguage) = authRepo.setAppLanguage(appLanguage)
}