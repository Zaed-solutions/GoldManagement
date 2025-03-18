package com.zaed.distributor.app.di

import com.zaed.common.ui.addcustomers.AddCustomersViewModel
import com.zaed.common.ui.addpurchase.AddPurchaseViewModel
import com.zaed.common.ui.customerdetails.CustomerDetailsViewModel
import com.zaed.common.ui.displaycustomers.DisplayCustomersViewModel
import com.zaed.common.ui.ingottransactions.IngotTransactionsViewModel
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsViewModel
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsViewModel
import com.zaed.distributor.ui.addproductsale.AddProductSaleViewModel
import com.zaed.distributor.ui.losses.LossesViewModel
import com.zaed.distributor.ui.sales.SalesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val distributorViewModelModule = module {
    viewModelOf(::DisplayCustomersViewModel)
    viewModelOf(::AddCustomersViewModel)
    viewModelOf(::CustomerDetailsViewModel)
    viewModelOf(::GoldSaleDetailsViewModel)
    viewModelOf(::AddProductSaleViewModel)
}
val appModule = module {
    viewModelOf(::SalesViewModel)
    viewModelOf(::AddPurchaseViewModel)
    viewModelOf(::ProductSaleDetailsViewModel)
    viewModelOf(::LossesViewModel)
    viewModelOf(::IngotTransactionsViewModel)
}