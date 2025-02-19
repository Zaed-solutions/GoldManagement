package com.zaed.distributor.ui.customerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import com.zaed.common.domain.FetchCustomerPaymentsUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerDetailsViewModel(
    private val fetchCustomerPaymentsUseCase: FetchCustomerPaymentsUseCase
):ViewModel(){
    private val _uiState = MutableStateFlow(CustomerDetailsUiState())
    val uiState = _uiState.asStateFlow()



    fun getCustomerTransactions(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loading = true)
            }
            fetchCustomerPaymentsUseCase(
                /*todo*/
                request = FetchCustomerPaymentsRequest(customerId)
            ).collect{result->
                result.onSuccess { data->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            payments = data
                                .sortedByDescending { it.createdAt }
                                .groupBy { it.createdAt.format(DateFormat.DATE_TIME)}
                        )
                    }
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = "Something went wrong"
                    )
                }
            }
        }
    }
}

data class CustomerDetailsUiState(
    val loading: Boolean = false,
    val error: String ="",
    val payments: Map<String, List<Payment>> = emptyMap(),
    val addPaymentSuccess: Boolean = false,
    val addPaymentError: String ="",
)
