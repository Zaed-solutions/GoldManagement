package com.zaed.manager.ui.storedetails

sealed interface StoreDetailsUiAction {
    data object OnBackClicked: StoreDetailsUiAction
    data class OnQueryChanged(val query: String): StoreDetailsUiAction
}