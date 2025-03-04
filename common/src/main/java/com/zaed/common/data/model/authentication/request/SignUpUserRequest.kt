package com.zaed.common.data.model.authentication.request

import com.zaed.common.data.model.store.Store
import com.zaed.common.data.model.authentication.UserRole

data class SignUpUserRequest(
    val fullName: String = "",
    val userName: String = "",
    val password: String = "",
    val role: UserRole = UserRole.NONE,
    val store: Store
)