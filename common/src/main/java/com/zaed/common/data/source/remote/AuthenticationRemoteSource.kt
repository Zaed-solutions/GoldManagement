package com.zaed.common.data.source.remote

import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.model.request.SignUpUserRequest
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteSource {
    suspend fun loginUser(request: LoginUserRequest): Result<User>
    suspend fun signUpUser(request: SignUpUserRequest): Result<User>
    fun fetchCurrentUser(userId: String): Flow<Result<User>>
}
