package com.zaed.distributor.app.di

import com.zaed.distributor.ui.addcustomers.AddCustomersViewModel
import com.zaed.distributor.ui.addproductsale.AddProductSaleViewModel
import com.zaed.distributor.ui.customerdetails.CustomerDetailsViewModel
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersViewModel
import com.zaed.distributor.ui.productsaledetails.ProductSaleDetailsViewModel
import com.zaed.distributor.ui.sales.SalesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val distributorViewModelModule = module {
    viewModelOf(::DisplayCustomersViewModel)
    viewModelOf(::AddCustomersViewModel)
    viewModelOf(::CustomerDetailsViewModel)
}
val appModule = module {
    viewModelOf(::SalesViewModel)
    viewModelOf(::AddProductSaleViewModel)
    viewModelOf(::ProductSaleDetailsViewModel)
}