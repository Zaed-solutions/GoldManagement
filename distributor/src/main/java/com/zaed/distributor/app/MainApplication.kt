package com.zaed.distributor.app

import android.app.Application
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
                viewModelModule
            )
        }
    }
}