package com.zaed.manager.ui.distributors

import com.zaed.common.data.model.authentication.User

data class DistributorsUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val allDistributors: List<User> = emptyList(),
    val displayedDistributors: List<User> = emptyList()
)
