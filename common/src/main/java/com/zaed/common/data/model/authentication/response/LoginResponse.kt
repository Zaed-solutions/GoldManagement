package com.zaed.common.data.model.authentication.response

import com.zaed.common.data.model.authentication.User

data class LoginResponse(
    val user: User? = null,
    val userExist: Boolean = true,
    val passwordCorrect: Boolean = true,
    )