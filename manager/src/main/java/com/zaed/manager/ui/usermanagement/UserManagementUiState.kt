package com.zaed.manager.ui.usermanagement

import com.zaed.common.data.model.authentication.User

data class UserManagementUiState(
    val isLoading: Boolean = true,
    val allUsers: List<User> = emptyList(),
    val displayedUsers: List<User> = emptyList(),
    val displayedRequests: List<User> = emptyList(),
    val displayedRejects: List<User> = emptyList(),
    val searchQuery: String = "",
)
