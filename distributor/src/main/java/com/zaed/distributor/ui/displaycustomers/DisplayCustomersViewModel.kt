package com.zaed.distributor.ui.displaycustomers

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.domain.customer.GetWholeSalesCustomersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DisplayCustomersViewModel(
    private val getWholeSalesCustomersUseCase: GetWholeSalesCustomersUseCase,
):ViewModel() {
    private val _state = MutableStateFlow(DisplayCustomersState())
    val state = _state.asStateFlow()
    init {
        getWholeSalesCustomers()
    }

    fun handleAction(action: DisplayWholeSalesCustomerUiAction) {
        when (action) {
            is DisplayWholeSalesCustomerUiAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            else -> {}
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                displayedCustomers = if(query.isNotEmpty()) it.customers.filter { customer ->
                    customer.name.contains(query, ignoreCase = true)
                } else  it.customers
            )
        }
    }

    private fun getWholeSalesCustomers() {
        viewModelScope.launch (Dispatchers.IO){
            getWholeSalesCustomersUseCase().collect{result->
                result.onSuccess { customers ->
                    _state.update {
                        it.copy(
                            customers = customers,
                            displayedCustomers = customers.sortedByDescending { it.debtAmount },
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
    }
}

data class DisplayCustomersState(
    val customers: List<WholeSaleCustomer> = emptyList(),
    val searchQuery: String = "",
    val displayedCustomers: List<WholeSaleCustomer> = emptyList(),
    val isLoading: Boolean = false,
    val error: DisplayCustomersScreenError? = null
)
enum class DisplayCustomersScreenError(@StringRes val message: Int){
    NO_INTERNET(
        com.zaed.common.R.string.no_internet_connection
    ),
    UNKNOWN(
        com.zaed.common.R.string.unknown_error
    )
}
