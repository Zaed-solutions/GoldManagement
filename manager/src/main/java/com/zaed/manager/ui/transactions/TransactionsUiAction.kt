package com.zaed.manager.ui.transactions

sealed interface TransactionsUiAction {
    data object OnShowNavDrawer: TransactionsUiAction
}