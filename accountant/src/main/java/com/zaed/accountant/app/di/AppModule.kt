package com.zaed.accountant.app.di

import com.zaed.accountant.ui.purchases.PurchasesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    viewModelOf(::PurchasesViewModel)
}