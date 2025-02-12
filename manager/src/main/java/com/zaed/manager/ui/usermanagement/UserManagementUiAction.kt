package com.zaed.manager.ui.usermanagement

import com.zaed.common.data.model.User

sealed interface UserManagementUiAction {
    data class UpdateSearchQuery(val searchQuery: String) : UserManagementUiAction
    data class ChangeApprovedStatus(val userId: String, val isAccepted: Boolean) : UserManagementUiAction
    data class DeleteUser(val userId: String) : UserManagementUiAction
    data class UpdateUser(val user: User) : UserManagementUiAction
}