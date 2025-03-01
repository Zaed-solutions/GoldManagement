package com.zaed.manager.ui.stores

import com.zaed.common.data.model.store.Store

data class StoresUiState(
    val isLoading : Boolean = false,
    val stores : List<Store> = emptyList(),
)
