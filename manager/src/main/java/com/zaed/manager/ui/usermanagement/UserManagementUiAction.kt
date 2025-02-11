package com.zaed.manager.ui.usermanagement

sealed interface UserManagementUiAction {
    data class UpdateSearchQuery(val searchQuery: String) : UserManagementUiAction
}