package com.zaed.cashier.app.di

import com.zaed.cashier.ui.MainViewModel
import com.zaed.cashier.ui.auth.login.LoginViewModel
import com.zaed.cashier.ui.auth.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
}