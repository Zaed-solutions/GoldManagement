package com.zaed.common.data.source.local

import com.zaed.common.data.model.authentication.LocalUser
import com.zaed.common.ui.util.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    fun getLocalUser(): Flow<Result<LocalUser>>
    suspend fun setLocalUser(value: LocalUser)
    fun fetchAppLanguage(): Flow<Result<AppLanguage>>
    suspend fun setAppLanguage(appLanguage: AppLanguage): Result<Unit>
}