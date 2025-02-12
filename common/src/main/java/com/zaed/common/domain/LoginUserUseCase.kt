package com.zaed.common.domain

import com.zaed.common.data.model.User
import com.zaed.common.data.model.request.LoginUserRequest
import com.zaed.common.data.repository.AuthenticationRepository
import com.zaed.common.ui.component.auth.login.LoginError

class LoginUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(request: LoginUserRequest):Result<User> {
       val result = authenticationRepository.loginUser(request)
         if(result.isSuccess){
            val user = result.getOrNull()
             return if (user?.role == request.role) {
                 Result.success(user)
             } else {
                 Result.failure(LoginError.InvalidRole())
             }
        }else{
            return Result.failure(result.exceptionOrNull()!!)
        }
    }
}

