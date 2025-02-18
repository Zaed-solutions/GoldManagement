package com.zaed.manager.ui.usermanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovalStatus
import com.zaed.common.data.model.request.DeleteUserRequest
import com.zaed.common.data.model.request.UpdateUserRequest
import com.zaed.common.domain.DeleteUserUseCase
import com.zaed.common.domain.FetchUsersUseCase
import com.zaed.common.domain.UpdateUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserManagementViewModel(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel() {
    private val TAG: String = "UserManagementVM"
    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch (Dispatchers.IO){
            fetchUsersUseCase().collect{ result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(allUsers = data.sortedBy { it.fullName })
                    }
                    filterData()
                }.onFailure {
                    Log.e(TAG, "fetchUsers: ${it.message}", it)
                }
            }
        }
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
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserUseCase(
                DeleteUserRequest(userId)
            ).onSuccess {
                Log.d(TAG, "deleteUser: success")
            }.onFailure {
                Log.e(TAG, "deleteUser: ${it.message}", it)
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            val updates = mapOf(
                "fullName" to user.fullName,
                "userName" to user.userName,
                "password" to user.password
            )
            updateUserUseCase(
                UpdateUserRequest(
                    userId = user.id,
                    updates = updates
                )
            ).onSuccess {
                Log.d(TAG, "updateUser: success")
            }.onFailure {
                Log.e(TAG, "updateUser: ${it.message}", it)
            }
        }
    }

    private fun updateApprovedStatus(userId: String, accepted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updates = mapOf(
                "approvalStatusType" to if(accepted) UserApprovalStatus.APPROVED else UserApprovalStatus.REJECTED
            )
            updateUserUseCase(
                UpdateUserRequest(
                    userId = userId,
                    updates = updates
                )
            ).onSuccess {
                Log.d(TAG, "updateApprovedStatus: success")
            }.onFailure {
                Log.e(TAG, "updateApprovedStatus: ${it.message}", it)
            }
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

    private fun filterData(searchQuery: String = "") {
        viewModelScope.launch (Dispatchers.Default){
            with(uiState.value.allUsers){
                if(searchQuery.isBlank()){
                    val displayedUsers = filter { it.approvalStatusType == UserApprovalStatus.APPROVED }
                    val displayedRequests = filter { it.approvalStatusType == UserApprovalStatus.PENDING }
                    val displayedRejects = filter { it.approvalStatusType == UserApprovalStatus.REJECTED }
                    _uiState.update {
                        it.copy(
                            displayedUsers = displayedUsers,
                            displayedRequests = displayedRequests,
                            displayedRejects = displayedRejects
                        )
                    }
                } else {
                    val filteredUsers = filter { it.fullName.contains(searchQuery, ignoreCase = true) }
                    val displayedUsers = filteredUsers.filter { it.approvalStatusType == UserApprovalStatus.APPROVED }
                    val displayedRequests = filteredUsers.filter { it.approvalStatusType == UserApprovalStatus.PENDING }
                    val displayedRejects = filteredUsers.filter { it.approvalStatusType == UserApprovalStatus.REJECTED }
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