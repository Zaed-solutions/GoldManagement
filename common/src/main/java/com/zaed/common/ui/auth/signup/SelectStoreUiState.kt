package com.zaed.common.ui.auth.signup

import com.zaed.common.data.model.store.Store

data class SelectStoreUiState(
    val stores: List<Store> = emptyList(),
    val selectedStore: Store = Store()
)