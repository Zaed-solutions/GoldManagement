package com.zaed.cashier.ui.loss

sealed interface LossUiAction {
    data class OnCreateNewLoss(
        val reason : String = "",
        val value : String = ""
    ) : LossUiAction
    data object OnAddNewLoss : LossUiAction
    data object ResetError : LossUiAction
    data object ResetSuccess : LossUiAction
    data object OnBack : LossUiAction
}