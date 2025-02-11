package com.zaed.manager.ui.usermanagement

import com.zaed.common.data.model.User

data class UserManagementUiState(
    val isLoading: Boolean = true,
    val allUsers: List<User> = emptyList(),
    val displayedUsers: List<User> = emptyList(),
    val displayedRequests: List<User> = emptyList(),
    val searchQuery: String = "",
)
