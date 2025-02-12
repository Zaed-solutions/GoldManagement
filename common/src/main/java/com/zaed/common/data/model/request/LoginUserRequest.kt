package com.zaed.common.data.model.request

data class LoginUserRequest(
    val userName: String = "",
    val password: String = "",
)