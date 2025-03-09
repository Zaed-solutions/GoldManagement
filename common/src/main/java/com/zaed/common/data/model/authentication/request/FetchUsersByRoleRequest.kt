package com.zaed.common.data.model.authentication.request

import com.zaed.common.data.model.authentication.UserRole

data class FetchUsersByRoleRequest(
    val role: UserRole
)
