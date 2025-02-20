package com.zaed.common.data.model.authentication.request

import com.zaed.common.data.model.authentication.UserRole

data class LoginUserRequest(
    val userName: String = "",
    val password: String = "",
    val role: UserRole = UserRole.NONE

)