package com.zaed.common.app.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zaed.common.data.repository.AuthenticationRepository
import com.zaed.common.data.repository.AuthenticationRepositoryImpl
import com.zaed.common.data.source.local.LocalStorage
import com.zaed.common.data.source.local.LocalStorageImpl
import com.zaed.common.data.source.remote.AuthenticationRemoteSource
import com.zaed.common.data.source.remote.AuthenticationRemoteSourceImpl
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.LoginUserUseCase
import com.zaed.common.domain.LogoutUserUseCase
import com.zaed.common.domain.SignUpUserUseCase
import com.zaed.common.ui.component.auth.MainViewModel
import com.zaed.common.ui.component.auth.login.LoginViewModel
import com.zaed.common.ui.component.auth.signup.SignUpViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val localSourceModule = module {
    singleOf(::LocalStorageImpl) { bind<LocalStorage>() }
}

val useCaseModule = module {
    singleOf(::LogoutUserUseCase)
    singleOf(::GetCurrentUserLoggedInUseCase)
    singleOf(::LoginUserUseCase)
    singleOf(::SignUpUserUseCase)
}
val repositoryModule = module {
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
}
val viewModelModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)
}
val remoteSourceModule = module {
    singleOf(::AuthenticationRemoteSourceImpl) {bind<AuthenticationRemoteSource>()}
    single<FirebaseFirestore> {
        val db = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings
        db
    }
    single<FirebaseCrashlytics> { Firebase.crashlytics }
}