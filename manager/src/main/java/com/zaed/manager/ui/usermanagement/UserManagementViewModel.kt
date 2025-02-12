package com.zaed.manager.ui.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserManagementViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        TODO("Not yet implemented")
    }

    fun handleAction(action: UserManagementUiAction){
        when(action){
            is UserManagementUiAction.UpdateSearchQuery -> updateSearchQuery(action.searchQuery)
            is UserManagementUiAction.ChangeApprovedStatus -> updateApprovedStatus(action.userId, action.isAccepted)
            is UserManagementUiAction.DeleteUser -> deleteUser(action.userId)
            is UserManagementUiAction.UpdateUser -> updateUser(action.user)
            else -> Unit
        }
    }

    private fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

    private fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    private fun updateApprovedStatus(userId: String, accepted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            //todo: update user approvement status
        }
    }

    private fun updateSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(searchQuery = searchQuery)
            }
            filterData(searchQuery = searchQuery)
        }
    }

    private fun filterData(searchQuery: String) {
        viewModelScope.launch (Dispatchers.Default){
            with(uiState.value.allUsers){
                if(searchQuery.isBlank()){
                    val displayedUsers = filter { it.approvementStatusType == UserApprovementStatusType.APPROVED }
                    val displayedRequests = filter { it.approvementStatusType == UserApprovementStatusType.PENDING }
                    val displayedRejects = filter { it.approvementStatusType == UserApprovementStatusType.REJECTED }
                    _uiState.update {
                        it.copy(
                            displayedUsers = displayedUsers,
                            displayedRequests = displayedRequests,
                            displayedRejects = displayedRejects
                        )
                    }
                } else {
                    val filteredUsers = filter { it.fullName.contains(searchQuery, ignoreCase = true) }
                    val displayedUsers = filteredUsers.filter { it.approvementStatusType == UserApprovementStatusType.APPROVED }
                    val displayedRequests = filteredUsers.filter { it.approvementStatusType == UserApprovementStatusType.PENDING }
                    val displayedRejects = filteredUsers.filter { it.approvementStatusType == UserApprovementStatusType.REJECTED }
                    _uiState.update {
                        it.copy(
                            displayedUsers = displayedUsers,
                            displayedRequests = displayedRequests,
                            displayedRejects = displayedRejects
                        )
                    }
                }
            }
        }
    }
}