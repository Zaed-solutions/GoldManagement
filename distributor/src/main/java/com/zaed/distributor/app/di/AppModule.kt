package com.zaed.distributor.app.di

import com.zaed.distributor.ui.addcustomers.AddCustomersViewModel
import com.zaed.distributor.ui.customerdetails.CustomerDetailsViewModel
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val distributorViewModelModule = module {
    viewModelOf(::DisplayCustomersViewModel)
    viewModelOf(::AddCustomersViewModel)
    viewModelOf(::CustomerDetailsViewModel)
}