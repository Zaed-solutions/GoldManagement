package com.zaed.manager.ui.usermanagement

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.UserRole

sealed interface UserManagementUiAction {
    data object ShowNavDrawer: UserManagementUiAction
    data class UpdateSearchQuery(val searchQuery: String) : UserManagementUiAction
    data class ChangeApprovedStatus(val userId: String, val isAccepted: Boolean) : UserManagementUiAction
    data class DeleteUser(val userId: String) : UserManagementUiAction
    data class UpdateUser(val user: User) : UserManagementUiAction
    data class UpdateUserRoleFilter(val role: UserRole) : UserManagementUiAction
}