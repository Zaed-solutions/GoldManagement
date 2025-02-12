package com.zaed.common.data.repository

import com.zaed.common.data.model.LocalUser
import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.model.request.SignUpUserRequest
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getUserApprovementStatus(): Flow<Result<LocalUser>>
    suspend fun loginUser(request: LoginUserRequest): Result<User>
    suspend fun logoutCurrentUser():Result<Unit>
    suspend fun signUpUser(request: SignUpUserRequest): Flow<Result<User>>
    fun getCurrentUser(): Flow<Result<User>>
}