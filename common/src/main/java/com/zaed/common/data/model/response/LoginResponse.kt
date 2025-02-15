package com.zaed.common.data.model.response

import com.zaed.common.data.model.User

data class LoginResponse(
    val user: User? = null,
    val userExist: Boolean = true,
    val passwordCorrect: Boolean = true,
    )