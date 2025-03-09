package com.zaed.manager.ui.distributors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.authentication.request.FetchUsersByRoleRequest
import com.zaed.common.domain.authentication.FetchUsersByRoleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DistributorsViewModel(
    private val fetchDistributorsUseCase: FetchUsersByRoleUseCase
): ViewModel() {
    private val TAG: String = DistributorsViewModel::class.java.simpleName
    private val _uiState = MutableStateFlow(DistributorsUiState())
    val uiState = _uiState.asStateFlow()
    init {
        fetchDistributors()
    }

    private fun fetchDistributors() {
        viewModelScope.launch (Dispatchers.IO){
            fetchDistributorsUseCase(
                FetchUsersByRoleRequest(
                    role = UserRole.DISTRIBUTOR
                )
            ).collect{ result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(isLoading = false, allDistributors = data)
                    }
                    filterDistributors()
                }.onFailure {
                    Log.e(TAG, "fetchDistributors: ${it.message}", it)
                }
            }
        }
    }

    private fun filterDistributors() {
        viewModelScope.launch (Dispatchers.Default){
            val searchQuery = _uiState.value.searchQuery
            if(searchQuery.isBlank()){
                _uiState.update { oldState ->
                    oldState.copy(displayedDistributors = oldState.allDistributors)
                }
            } else {
                val filteredDistributors = _uiState.value.allDistributors.filter {
                    it.fullName.contains(searchQuery, ignoreCase = true)
                }
                _uiState.update { oldState ->
                    oldState.copy(displayedDistributors = filteredDistributors)
                }
            }
        }
    }

    fun handleAction(action: DistributorsUiAction){
        when(action){
            is DistributorsUiAction.UpdateSearchQuery -> updateSearchQueryAndFilter(action.query)
            else -> Unit
        }
    }

    private fun updateSearchQueryAndFilter(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(searchQuery = query)
            }
            filterDistributors()
        }
    }
}