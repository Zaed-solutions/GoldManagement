package com.zaed.common.data.source.local

import com.zaed.common.data.model.authentication.LocalUser
import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    fun getLocalUser(): Flow<Result<LocalUser>>
    suspend fun setLocalUser(value: LocalUser)
}