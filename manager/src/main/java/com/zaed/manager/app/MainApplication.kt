package com.zaed.manager.app

import android.app.Application
import com.zaed.common.app.di.localSourceModule
import com.zaed.common.app.di.remoteSourceModule
import com.zaed.common.app.di.repositoryModule
import com.zaed.common.app.di.useCaseModule
import com.zaed.common.app.di.viewModelModule
import com.zaed.manager.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                appModule,
                useCaseModule,
                localSourceModule,
                remoteSourceModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}