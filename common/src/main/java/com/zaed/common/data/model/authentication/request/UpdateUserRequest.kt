package com.zaed.common.data.model.authentication.request

data class UpdateUserRequest(
    val userId: String = "",
    val updates: Map<String, Any> = mapOf()
)
