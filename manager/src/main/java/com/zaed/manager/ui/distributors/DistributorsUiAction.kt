package com.zaed.manager.ui.distributors

import com.zaed.common.data.model.authentication.User

sealed interface DistributorsUiAction {
    data object OnShowNavDrawer: DistributorsUiAction
    data class OnDistributorClicked(val distributor: User): DistributorsUiAction
    data class UpdateSearchQuery(val query: String): DistributorsUiAction
}