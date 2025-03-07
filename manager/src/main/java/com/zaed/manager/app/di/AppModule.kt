package com.zaed.manager.app.di

import com.zaed.manager.ui.distributordetails.DistributorDetailsViewModel
import com.zaed.manager.ui.distributors.DistributorsViewModel
import com.zaed.manager.ui.storedetails.StoreDetailsViewModel
import com.zaed.manager.ui.stores.StoresViewModel
import com.zaed.manager.ui.usermanagement.UserManagementViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::UserManagementViewModel)
    viewModelOf(::StoresViewModel)
    viewModelOf(::StoreDetailsViewModel)
    viewModelOf(::DistributorsViewModel)
    viewModelOf(::DistributorDetailsViewModel)
}