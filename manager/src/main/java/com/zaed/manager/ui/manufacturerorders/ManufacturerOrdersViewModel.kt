package com.zaed.manager.ui.manufacturerorders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.data.model.manufacturerorder.request.AddManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.DeleteManufacturerOrderRequest
import com.zaed.common.data.model.manufacturerorder.request.UpdateManufacturerOrderRequest
import com.zaed.common.domain.manufacturerorder.AddManufacturerOrderUseCase
import com.zaed.common.domain.manufacturerorder.DeleteManufacturerOrderUseCase
import com.zaed.common.domain.manufacturerorder.FetchManufacturerOrdersUseCase
import com.zaed.common.domain.manufacturerorder.UpdateManufacturerOrderUseCase
import com.zaed.manager.ui.manufacturerorders.components.ManufacturerOrdersFilter
import com.zaed.manager.ui.manufacturerorders.components.OrderStatusFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ManufacturerOrdersViewModel(
    private val fetchOrdersUseCase: FetchManufacturerOrdersUseCase,
    private val addOrderUseCase: AddManufacturerOrderUseCase,
    private val updateOrderUseCase: UpdateManufacturerOrderUseCase,
    private val deleteOrderUseCase: DeleteManufacturerOrderUseCase
): ViewModel() {
    private val TAG: String = "ManufacturerOrdersViewModel"
    private val _uiState = MutableStateFlow(ManufacturerOrdersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch (Dispatchers.IO){
            fetchOrdersUseCase().collect{result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(
                            allOrders = data
                        )
                    }
                    filterData()
                }.onFailure {
                    Log.e(TAG, "fetchOrders: ${it.message}", it)
                }
            }
        }
    }

    fun handleAction(action: ManufacturerOrdersUiAction){
        when(action){
            is ManufacturerOrdersUiAction.DeleteManufacturerOrder -> deleteOrder(action.order)
            is ManufacturerOrdersUiAction.SaveManufacturerOrder -> saveOrder(action.order)
            is ManufacturerOrdersUiAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            is ManufacturerOrdersUiAction.UpdateFilter -> updateFilter(action.filter)
            else -> Unit
        }
    }

    private fun updateFilter(filter: ManufacturerOrdersFilter) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = true,
                    filter = filter
                )
            }
            filterData()
        }
    }

    private fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(searchQuery = query, isLoading = true)
            }
            filterData()
        }
    }

    private fun filterData() {
        viewModelScope.launch(Dispatchers.Default) {
            val filter = uiState.value.filter
            if (!filter.isFiltered && uiState.value.searchQuery.isBlank()) {
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        displayedOrders = oldState.allOrders
                    )
                }
                return@launch
            }

            val filteredOrders = uiState.value.allOrders.filter { order ->
                val dateInRange = when {
                    !filter.isFiltered -> true
                    filter.startDate != null && filter.endDate != null ->
                        order.createdAt in filter.startDate..filter.endDate

                    filter.startDate != null ->
                        order.createdAt >= filter.startDate

                    filter.endDate != null ->
                        order.createdAt <= filter.endDate

                    else -> true
                }
                val statusMatch = order.closed == (filter.orderStatus == OrderStatusFilter.CLOSED) || filter.orderStatus == OrderStatusFilter.ALL
                val queryMatch = order.orderNumber.contains(uiState.value.searchQuery)
                dateInRange && queryMatch && statusMatch
            }
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = false,
                    displayedOrders = filteredOrders
                )
            }
        }
    }

    private fun saveOrder(order: ManufacturerOrder) {
        if(order.id.isBlank()){
            addOrder(order)
        } else {
            updateOrder(order)
        }
    }

    private fun updateOrder(order: ManufacturerOrder) {
        viewModelScope.launch (Dispatchers.IO){
            updateOrderUseCase(
                UpdateManufacturerOrderRequest(
                    order = order
                )
            ).onSuccess {
                Log.d(TAG, "updateOrder: success")
            }.onFailure {
                Log.e(TAG, "updateOrder: ${it.message}", it)
            }
        }
    }

    private fun addOrder(order: ManufacturerOrder) {
        viewModelScope.launch (Dispatchers.IO){
            addOrderUseCase(
                AddManufacturerOrderRequest(
                    order = order.copy(createdAt = Date())
                )
            ).onSuccess {
                Log.d(TAG, "addOrder: success")
            }.onFailure {
                Log.e(TAG, "addOrder: ${it.message}", it)
            }
        }
    }

    private fun deleteOrder(order: ManufacturerOrder) {
        viewModelScope.launch (Dispatchers.IO){
            deleteOrderUseCase(
                DeleteManufacturerOrderRequest(
                    order = order
                )
            ).onSuccess {
                Log.d(TAG, "deleteOrder: success")
            }.onFailure {
                Log.e(TAG, "deleteOrder: ${it.message}", it)
            }
        }
    }
}