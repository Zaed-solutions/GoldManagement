package com.zaed.common.data.model.request

import com.zaed.common.data.model.UserRole

data class SignUpUserRequest (
    val fullName :String = "",
    val userName: String = "",
    val password: String = "",
    val role: UserRole = UserRole.NONE
)