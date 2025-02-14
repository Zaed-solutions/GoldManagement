package com.zaed.cashier.app.di

import com.zaed.cashier.ui.sales.SalesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.zaed.cashier.data.repository.LossRepository
import com.zaed.cashier.data.repository.LossRepositoryImpl
import com.zaed.cashier.data.source.LossRemoteDataSource
import com.zaed.cashier.data.source.LossRemoteDataSourceImpl
import com.zaed.cashier.domain.loss.CreateNewLossUseCase
import com.zaed.cashier.domain.loss.GetAllLossesUseCase
import com.zaed.cashier.ui.loss.LossViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf




val appModule = module {
    viewModelOf(::SalesViewModel)
    viewModelOf(_root_ide_package_.com.zaed.cashier.ui.sales.details::SaleDetailsViewModel)
    viewModelOf(::AddSaleViewModel)
    viewModelOf(::LossViewModel)
}