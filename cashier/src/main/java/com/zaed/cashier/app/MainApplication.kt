package com.zaed.cashier.app

import android.app.Application
import com.zaed.cashier.app.di.cashierRemoteSource
import com.zaed.cashier.app.di.cashierRepositoryModule
import com.zaed.cashier.app.di.cashierUseCaseModule
import com.zaed.cashier.app.di.cashierViewModelModule
import com.zaed.common.app.di.localSourceModule
import com.zaed.common.app.di.remoteSourceModule
import com.zaed.common.app.di.repositoryModule
import com.zaed.common.app.di.useCaseModule
import com.zaed.common.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                useCaseModule,
                localSourceModule,
                remoteSourceModule,
                repositoryModule,
                viewModelModule,
                cashierViewModelModule,
                cashierUseCaseModule,
                cashierRemoteSource,
                cashierRepositoryModule
            )
        }
    }
}