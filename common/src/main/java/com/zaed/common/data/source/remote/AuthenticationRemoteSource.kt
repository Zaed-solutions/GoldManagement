package com.zaed.common.data.source.remote

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.request.DeleteUserRequest
import com.zaed.common.data.model.authentication.request.FetchDistributorRequest
import com.zaed.common.data.model.authentication.request.LoginUserRequest
import com.zaed.common.data.model.authentication.request.SignUpUserRequest
import com.zaed.common.data.model.authentication.request.UpdateUserRequest
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteSource {
    suspend fun loginUser(request: LoginUserRequest): Result<User>
    suspend fun signUpUser(request: SignUpUserRequest): Result<User>
    fun fetchCurrentUser(userId: String): Flow<Result<User>>
    fun fetchUsers(): Flow<Result<List<User>>>
    suspend fun updateUser(request: UpdateUserRequest): Result<Unit>
    suspend fun deleteUser(request: DeleteUserRequest): Result<Unit>
    fun fetchDistributors(): Flow<Result<List<User>>>
    suspend fun fetchDistributor(request: FetchDistributorRequest): Result<User>
}
