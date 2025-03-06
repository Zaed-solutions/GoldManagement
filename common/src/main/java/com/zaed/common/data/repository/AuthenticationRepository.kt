package com.zaed.common.data.repository

import com.zaed.common.data.model.authentication.LocalUser
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.request.DeleteUserRequest
import com.zaed.common.data.model.authentication.request.LoginUserRequest
import com.zaed.common.data.model.authentication.request.SignUpUserRequest
import com.zaed.common.data.model.authentication.request.UpdateUserRequest
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getUserApprovalStatus(): Flow<Result<LocalUser>>
    suspend fun loginUser(request: LoginUserRequest): Result<User>
    suspend fun logoutCurrentUser():Result<Unit>
    suspend fun signUpUser(request: SignUpUserRequest): Result<User>
    fun getCurrentUser(): Flow<Result<User>>
    fun fetchUsers(): Flow<Result<List<User>>>
    suspend fun updateUser(request: UpdateUserRequest): Result<Unit>
    suspend fun deleteUser(request: DeleteUserRequest): Result<Unit>
    fun fetchDistributors(): Flow<Result<List<User>>>
}