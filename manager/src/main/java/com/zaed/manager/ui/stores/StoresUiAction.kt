package com.zaed.manager.ui.stores

import com.zaed.common.data.model.store.Store

sealed interface StoresUiAction {
    data object OnShowNavDrawer : StoresUiAction
    data class OnStoreClicked(val storeId: String) : StoresUiAction
    data class OnAddStore(val store: Store) : StoresUiAction
    data class OnUpdateStore(val store: Store) : StoresUiAction
    data class OnDeleteStore(val store: Store) : StoresUiAction
}