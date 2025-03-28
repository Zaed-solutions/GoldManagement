package com.zaed.common.ui.displaycustomers

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.customer.DeleteWholeSaleCustomerUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DisplayCustomersViewModel(
    private val getWholeSalesCustomersUseCase: GetWholeSalesCustomersUseCase,
    private val deleteWholeSaleCustomerUseCase: DeleteWholeSaleCustomerUseCase,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DisplayCustomersState())
    val state = _state.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            currentDistributor = data
                        )
                    }
                    if (data.role == UserRole.MANAGER || data.role == UserRole.ACCOUNTANT) {
                        getWholeSalesCustomers("")
                    } else {
                        getWholeSalesCustomers(data.id)
                    }
                }.onFailure {
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    fun handleAction(action: DisplayWholeSalesCustomerUiAction) {
        when (action) {
            is DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            is DisplayWholeSalesCustomerUiAction.OnCustomerDeleted -> deleteCustomer(action.customer)
            else -> {}
        }
    }

    private fun deleteCustomer(customer: WholeSaleCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            deleteWholeSaleCustomerUseCase(customer.id)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                        )
                    }
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = DisplayCustomersScreenError.UNKNOWN,
                        )
                    }
                }
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                isLoading = true
            )
        }
        filterData()
    }

    private fun filterData(){
        viewModelScope.launch (Dispatchers.Default){
            val query = state.value.searchQuery
            val filteredGoldCustomers = state.value.allGoldCustomers.filter { it.name.contains(query, ignoreCase = true) }
            val filteredSilverCustomers = state.value.allSilverCustomers.filter { it.name.contains(query, ignoreCase = true) }
            _state.update {
                it.copy(
                    isLoading = false,
                    filteredGoldCustomers = filteredGoldCustomers,
                    filteredSilverCustomers = filteredSilverCustomers
                )
            }
        }
    }

    private fun getWholeSalesCustomers(distributorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getWholeSalesCustomersUseCase(distributorId).collect { result ->
                result.onSuccess { customers ->
                    val grouperCustomers = customers.sortedBy { it.name }.groupBy { it.type }
                    _state.update {
                        it.copy(
                            allGoldCustomers = grouperCustomers[CustomerType.GOLD]?: emptyList(),
                            allSilverCustomers = grouperCustomers[CustomerType.SILVER]?: emptyList(),
                            error = null,
                        )
                    }
                    filterData()
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = DisplayCustomersScreenError.UNKNOWN,
                        )
                    }
                }
            }
        }
    }
}

data class DisplayCustomersState(
    val allGoldCustomers: List<WholeSaleCustomer> = emptyList(),
    val allSilverCustomers: List<WholeSaleCustomer> = emptyList(),
    val searchQuery: String = "",
    val filteredGoldCustomers: List<WholeSaleCustomer> = emptyList(),
    val filteredSilverCustomers: List<WholeSaleCustomer> = emptyList(),
    val isLoading: Boolean = false,
    val error: DisplayCustomersScreenError? = null,
    val currentDistributor: User = User()
)

enum class DisplayCustomersScreenError(@StringRes val message: Int) {
    NO_INTERNET(
        com.zaed.common.R.string.no_internet_connection
    ),
    UNKNOWN(
        com.zaed.common.R.string.unknown_error
    )
}
