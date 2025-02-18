package com.zaed.common.app.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zaed.common.data.repository.AuthenticationRepository
import com.zaed.common.data.repository.AuthenticationRepositoryImpl
import com.zaed.common.data.repository.LossRepository
import com.zaed.common.data.repository.LossRepositoryImpl
import com.zaed.common.data.repository.CategoryRepository
import com.zaed.common.data.repository.CategoryRepositoryImpl
import com.zaed.common.data.repository.SaleRepository
import com.zaed.common.data.repository.SaleRepositoryImpl
import com.zaed.common.data.repository.StoreRemoteDataSource
import com.zaed.common.data.repository.StoreRemoteDataSourceImpl
import com.zaed.common.data.repository.StoreRepository
import com.zaed.common.data.repository.StoreRepositoryImpl
import com.zaed.common.data.source.local.LocalStorage
import com.zaed.common.data.source.local.LocalStorageImpl
import com.zaed.common.data.source.remote.AuthenticationRemoteSource
import com.zaed.common.data.source.remote.AuthenticationRemoteSourceImpl
import com.zaed.common.data.source.remote.LossRemoteDataSource
import com.zaed.common.data.source.remote.LossRemoteDataSourceImpl
import com.zaed.common.data.source.remote.CategoryRemoteSource
import com.zaed.common.data.source.remote.CategoryRemoteSourceImpl
import com.zaed.common.data.source.remote.SaleRemoteSource
import com.zaed.common.data.source.remote.SaleRemoteSourceImpl
import com.zaed.common.domain.AddStoreSaleUseCase
import com.zaed.common.domain.CreateNewLossUseCase
import com.zaed.common.domain.DeleteLossUseCase
import com.zaed.common.domain.DeleteStoreSaleUseCase
import com.zaed.common.domain.DeleteUserUseCase
import com.zaed.common.domain.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.DeleteWholesaleProductSaleUseCase
import com.zaed.common.domain.FetchAllCategoriesUseCase
import com.zaed.common.domain.FetchDistributorSalesUseCase
import com.zaed.common.domain.FetchStoreSalesUseCase
import com.zaed.common.domain.FetchUsersUseCase
import com.zaed.common.domain.GetStoreLossesUseCase
import com.zaed.common.domain.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.GetStoreSaleUseCase
import com.zaed.common.domain.GetStoresUseCase
import com.zaed.common.domain.LoginUserUseCase
import com.zaed.common.domain.LogoutUserUseCase
import com.zaed.common.domain.SignUpUserUseCase
import com.zaed.common.domain.UpdateLossUseCase
import com.zaed.common.domain.UpdateStoreSaleUseCase
import com.zaed.common.domain.UpdateUserUseCase
import com.zaed.common.ui.auth.MainViewModel
import com.zaed.common.ui.auth.login.LoginViewModel
import com.zaed.common.ui.auth.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val localSourceModule = module {
    singleOf(::LocalStorageImpl) { bind<LocalStorage>() }
}

val useCaseModule = module {
    singleOf(::LogoutUserUseCase)
    singleOf(::GetCurrentUserLoggedInUseCase)
    singleOf(::LoginUserUseCase)
    singleOf(::SignUpUserUseCase)
    singleOf(::FetchUsersUseCase)
    singleOf(::UpdateUserUseCase)
    singleOf(::DeleteUserUseCase)
    singleOf(::FetchStoreSalesUseCase)
    singleOf(::AddStoreSaleUseCase)
    singleOf(::FetchAllCategoriesUseCase)
    singleOf(::GetStoreLossesUseCase)
    singleOf(::CreateNewLossUseCase)
    singleOf(::GetStoresUseCase)
    singleOf(::GetStoreSaleUseCase)
    singleOf(::UpdateLossUseCase)
    singleOf(::DeleteLossUseCase)
    singleOf(::DeleteStoreSaleUseCase)
    singleOf(::UpdateStoreSaleUseCase)
    singleOf(::FetchDistributorSalesUseCase)
    singleOf(::DeleteWholesaleProductSaleUseCase)
    singleOf(::DeleteWholesaleGoldSaleUseCase)
}
val repositoryModule = module {
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::SaleRepositoryImpl) { bind<SaleRepository>() }
    singleOf(::LossRepositoryImpl) { bind<LossRepository>()}
    singleOf(::StoreRepositoryImpl) { bind<StoreRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }
}
val viewModelModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)
}
val remoteSourceModule = module {
    singleOf(::AuthenticationRemoteSourceImpl) {bind<AuthenticationRemoteSource>()}
    singleOf(::SaleRemoteSourceImpl) { bind<SaleRemoteSource>() }
    singleOf(::CategoryRemoteSourceImpl) { bind<CategoryRemoteSource>() }
    singleOf(::LossRemoteDataSourceImpl) { bind<LossRemoteDataSource>() }
    singleOf(::StoreRemoteDataSourceImpl) { bind<StoreRemoteDataSource>() }
    single<FirebaseFirestore> {
        val db = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build()
            )
            .build()
        db.firestoreSettings = settings
        db
    }
    single<FirebaseCrashlytics> { Firebase.crashlytics }
}