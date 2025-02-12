package com.zaed.common.data.repository

import com.zaed.common.data.model.LocalUser
import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.model.request.SignUpUserRequest
import com.zaed.common.data.source.local.LocalStorage
import com.zaed.common.data.source.remote.AuthenticationRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthenticationRepositoryImpl(
    private val localStorage: LocalStorage,
    private val remoteSource : AuthenticationRemoteSource
) : AuthenticationRepository {
    override fun getUserApprovementStatus(): Flow<Result<LocalUser>> =
        localStorage.getLocalUser()

    override suspend fun loginUser(request: LoginUserRequest): Result<User> {
        val response = remoteSource.loginUser(request)
        response.onSuccess {
            localStorage.setLocalUser(
                LocalUser(
                    userId = it.id
                )
            )
        }.onFailure {
            localStorage.setLocalUser(
                LocalUser(
                    userId = ""
                )
            )
        }
        return response
    }

    override suspend fun signUpUser(request: SignUpUserRequest): Result<User> {
        val response = remoteSource.signUpUser(request)
            response.onSuccess {
                localStorage.setLocalUser(
                    LocalUser(
                        userId = it.id
                    )
                )

            }.onFailure {
                localStorage.setLocalUser(
                    LocalUser(
                        userId = ""
                    )
                )
            }
        return response
    }
    override suspend fun logoutCurrentUser(): Result<Unit> {
        localStorage.setLocalUser(
            LocalUser(
                userId = ""
            )
        )
        return Result.success(Unit)
    }

    override  fun getCurrentUser(): Flow<Result<User>> = flow{
        localStorage.getLocalUser().collect{result->
            result.onSuccess {localUser->
                if(localUser.userId.isEmpty()){
                    emit(Result.failure(Exception("User not logged in")))
                }else {
                    remoteSource.fetchCurrentUser(localUser.userId).collect { serverResult ->
                        serverResult.onSuccess {
                            emit(Result.success(it))
                        }.onFailure {
                            emit(Result.failure(it))
                        }
                    }
                }
            }.onFailure {
                emit(Result.failure(it))
            }
        }
    }
}