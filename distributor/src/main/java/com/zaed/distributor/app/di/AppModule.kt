package com.zaed.distributor.app.di

import com.zaed.distributor.ui.sales.SalesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::SalesViewModel)
}