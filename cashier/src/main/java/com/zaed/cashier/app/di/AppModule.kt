package com.zaed.cashier.app.di

import com.zaed.cashier.ui.addsale.AddSaleViewModel
import com.zaed.cashier.ui.loss.LossViewModel
import com.zaed.cashier.ui.sales.SalesViewModel
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    viewModelOf(::SalesViewModel)
    viewModelOf(::SaleDetailsViewModel)
    viewModelOf(::AddSaleViewModel)
    viewModelOf(::LossViewModel)
}