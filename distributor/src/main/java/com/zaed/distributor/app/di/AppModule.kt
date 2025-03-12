package com.zaed.distributor.app.di

import com.zaed.common.ui.addcustomers.AddCustomersViewModel
import com.zaed.common.ui.customerdetails.CustomerDetailsViewModel
import com.zaed.common.ui.displaycustomers.DisplayCustomersViewModel
import com.zaed.common.ui.ingottransactions.IngotTransactionsViewModel
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsViewModel
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsViewModel
import com.zaed.common.ui.addGoldSale.AddGoldSaleViewModel
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
}
val appModule = module {
    viewModelOf(::SalesViewModel)
    viewModelOf(::AddProductSaleViewModel)
    viewModelOf(::ProductSaleDetailsViewModel)
    viewModelOf(::LossesViewModel)
    viewModelOf(::IngotTransactionsViewModel)
}