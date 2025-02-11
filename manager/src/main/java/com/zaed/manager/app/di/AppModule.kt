package com.zaed.manager.app.di

import com.zaed.manager.ui.usermanagement.UserManagementViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::UserManagementViewModel)
}