package com.zaed.common.data.model.request

data class UpdateUserRequest(
    val userId: String = "",
    val updates: Map<String, Any> = mapOf()
)
