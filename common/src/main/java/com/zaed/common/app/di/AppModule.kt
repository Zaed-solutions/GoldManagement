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
import com.zaed.common.data.repository.CategoryRepository
import com.zaed.common.data.repository.CategoryRepositoryImpl
import com.zaed.common.data.repository.ChequeRepository
import com.zaed.common.data.repository.ChequeRepositoryImpl
import com.zaed.common.data.repository.InventoryRepository
import com.zaed.common.data.repository.InventoryRepositoryImpl
import com.zaed.common.data.repository.LossRepository
import com.zaed.common.data.repository.LossRepositoryImpl
import com.zaed.common.data.repository.ManufacturerOrderRepository
import com.zaed.common.data.repository.ManufacturerOrderRepositoryImpl
import com.zaed.common.data.repository.PaymentRepository
import com.zaed.common.data.repository.PaymentRepositoryImpl
import com.zaed.common.data.repository.SaleRepository
import com.zaed.common.data.repository.SaleRepositoryImpl
import com.zaed.common.data.repository.StoreRepository
import com.zaed.common.data.repository.StoreRepositoryImpl
import com.zaed.common.data.repository.SupplierRepository
import com.zaed.common.data.repository.SupplierRepositoryImpl
import com.zaed.common.data.repository.WholeSalesCustomerRepository
import com.zaed.common.data.repository.WholeSalesCustomerRepositoryImpl
import com.zaed.common.data.source.local.LocalStorage
import com.zaed.common.data.source.local.LocalStorageImpl
import com.zaed.common.data.source.remote.AuthenticationRemoteSource
import com.zaed.common.data.source.remote.AuthenticationRemoteSourceImpl
import com.zaed.common.data.source.remote.CategoryRemoteSource
import com.zaed.common.data.source.remote.CategoryRemoteSourceImpl
import com.zaed.common.data.source.remote.ChequeRemoteSource
import com.zaed.common.data.source.remote.ChequeRemoteSourceImpl
import com.zaed.common.data.source.remote.InventoryRemoteSource
import com.zaed.common.data.source.remote.InventoryRemoteSourceImpl
import com.zaed.common.data.source.remote.LossRemoteDataSource
import com.zaed.common.data.source.remote.LossRemoteDataSourceImpl
import com.zaed.common.data.source.remote.ManufacturerOrderRemoteSource
import com.zaed.common.data.source.remote.ManufacturerOrderRemoteSourceImpl
import com.zaed.common.data.source.remote.PaymentRemoteDataSource
import com.zaed.common.data.source.remote.PaymentRemoteDataSourceImpl
import com.zaed.common.data.source.remote.SaleRemoteSource
import com.zaed.common.data.source.remote.SaleRemoteSourceImpl
import com.zaed.common.data.source.remote.StoreRemoteDataSource
import com.zaed.common.data.source.remote.StoreRemoteDataSourceImpl
import com.zaed.common.data.source.remote.SupplierRemoteSource
import com.zaed.common.data.source.remote.SupplierRemoteSourceImpl
import com.zaed.common.data.source.remote.WholeSalesCustomerRemoteDataSource
import com.zaed.common.data.source.remote.WholeSalesCustomerRemoteDataSourceImpl
import com.zaed.common.domain.authentication.DeleteUserUseCase
import com.zaed.common.domain.authentication.FetchDistributorUseCase
import com.zaed.common.domain.authentication.FetchUsersByRoleUseCase
import com.zaed.common.domain.authentication.FetchUsersUseCase
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.authentication.LoginUserUseCase
import com.zaed.common.domain.authentication.LogoutUserUseCase
import com.zaed.common.domain.authentication.SignUpUserUseCase
import com.zaed.common.domain.authentication.UpdateUserUseCase
import com.zaed.common.domain.category.FetchAllCategoriesUseCase
import com.zaed.common.domain.cheque.AddManagerChequeUseCase
import com.zaed.common.domain.cheque.AddSalesChequeUseCase
import com.zaed.common.domain.cheque.FetchManagerChequesUseCase
import com.zaed.common.domain.cheque.FetchSalesChequesUseCase
import com.zaed.common.domain.cheque.UpdateManagerCheckStatusUseCase
import com.zaed.common.domain.cheque.UpdateManagerChequeUseCase
import com.zaed.common.domain.cheque.UpdateSaleCheckStatusUseCase
import com.zaed.common.domain.cheque.UpdateSalesChequeUseCase
import com.zaed.common.domain.customer.AddWholeSaleCustomerUseCase
import com.zaed.common.domain.customer.DeleteWholeSaleCustomerUseCase
import com.zaed.common.domain.customer.EditWholeSalesCustomerUseCase
import com.zaed.common.domain.customer.FetchAllWholeCustomersUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomerSalesUseCase
import com.zaed.common.domain.customer.FetchWholesaleCustomersByNameUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomerUseCase
import com.zaed.common.domain.customer.GetWholeSalesCustomersUseCase
import com.zaed.common.domain.inventory.AddInventoryUseCase
import com.zaed.common.domain.inventory.FetchInventoriesUseCase
import com.zaed.common.domain.inventory.UpdateInventoryUseCase
import com.zaed.common.domain.loss.AddDistributorLossUseCase
import com.zaed.common.domain.loss.AddManagerLossUseCase
import com.zaed.common.domain.loss.ConvertLossesToDatedLossesUseCase
import com.zaed.common.domain.loss.CreateNewLossUseCase
import com.zaed.common.domain.loss.DeleteLossUseCase
import com.zaed.common.domain.loss.DeleteManagerLossUseCase
import com.zaed.common.domain.loss.FetchDistributorLossesUseCase
import com.zaed.common.domain.loss.FetchManagerLossesUseCase
import com.zaed.common.domain.loss.FetchStoreLossesUseCase
import com.zaed.common.domain.loss.GetStoreLossesUseCase
import com.zaed.common.domain.loss.UpdateDistributorLossUseCase
import com.zaed.common.domain.loss.UpdateLossUseCase
import com.zaed.common.domain.loss.UpdateManagerLossUseCase
import com.zaed.common.domain.manufacturerorder.AddManufacturerOrderUseCase
import com.zaed.common.domain.manufacturerorder.DeleteManufacturerOrderUseCase
import com.zaed.common.domain.manufacturerorder.FetchManufacturerOrdersUseCase
import com.zaed.common.domain.manufacturerorder.UpdateManufacturerOrderUseCase
import com.zaed.common.domain.payment.AddNewPaymentUseCase
import com.zaed.common.domain.payment.DeletePaymentUseCase
import com.zaed.common.domain.payment.EditPaymentUseCase
import com.zaed.common.domain.payment.FetchCustomerPaymentsUseCase
import com.zaed.common.domain.payment.FetchGoldPaymentsByIdsUseCase
import com.zaed.common.domain.payment.FetchMoneyPaymentsByIdsUseCase
import com.zaed.common.domain.payment.FetchSupplierPaymentsUseCase
import com.zaed.common.domain.sale.AddGoldSaleUseCase
import com.zaed.common.domain.sale.AddIngotTransactionUseCase
import com.zaed.common.domain.sale.AddStoreSaleUseCase
import com.zaed.common.domain.sale.AddWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.ConvertIngotTransactionsToDatedUseCase
import com.zaed.common.domain.sale.ConvertSalesToDatedSalesUseCase
import com.zaed.common.domain.sale.DeleteStoreSaleUseCase
import com.zaed.common.domain.sale.DeleteWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.DeleteWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.FetchAllDistributorsSalesUseCase
import com.zaed.common.domain.sale.FetchAllStoreSalesUseCase
import com.zaed.common.domain.sale.FetchDistributorSalesUseCase
import com.zaed.common.domain.sale.FetchIngotTransactionsUseCase
import com.zaed.common.domain.sale.FetchStoreSalesUseCase
import com.zaed.common.domain.sale.FetchWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.FetchWholesaleProductSaleUseCase
import com.zaed.common.domain.sale.GetStoreSaleUseCase
import com.zaed.common.domain.sale.UpdateIngotTransactionUseCase
import com.zaed.common.domain.sale.UpdateStoreSaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleGoldSaleUseCase
import com.zaed.common.domain.sale.UpdateWholesaleProductSaleUseCase
import com.zaed.common.domain.store.AddStoreUseCase
import com.zaed.common.domain.store.DeleteStoreUseCase
import com.zaed.common.domain.store.FetchStoreByIdUseCase
import com.zaed.common.domain.store.GetStoresUseCase
import com.zaed.common.domain.store.UpdateStoreUseCase
import com.zaed.common.domain.supplier.AddSupplierUseCase
import com.zaed.common.domain.supplier.DeleteSupplierUseCase
import com.zaed.common.domain.supplier.FetchSuppliersUseCase
import com.zaed.common.domain.supplier.UpdateSupplierUseCase
import com.zaed.common.ui.addGoldSale.AddGoldSaleViewModel
import com.zaed.common.ui.addcustomers.AddCustomersViewModel
import com.zaed.common.ui.auth.MainViewModel
import com.zaed.common.ui.auth.login.LoginViewModel
import com.zaed.common.ui.auth.signup.SignUpViewModel
import com.zaed.common.ui.customerdetails.CustomerDetailsViewModel
import com.zaed.common.ui.displaycustomers.DisplayCustomersViewModel
import com.zaed.common.ui.ingottransactions.IngotTransactionsViewModel
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsViewModel
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsViewModel
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsViewModel
import com.zaed.common.ui.suppliers.SuppliersViewModel
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
    singleOf(::FetchWholesaleGoldSaleUseCase)
    singleOf(::FetchWholesaleProductSaleUseCase)
    singleOf(::AddWholeSaleCustomerUseCase)
    singleOf(::GetWholeSalesCustomersUseCase)
    singleOf(::FetchCustomerPaymentsUseCase)
    singleOf(::AddNewPaymentUseCase)
    singleOf(::FetchMoneyPaymentsByIdsUseCase)
    singleOf(::FetchWholesaleCustomersByNameUseCase)
    singleOf(::GetWholeSalesCustomerUseCase)
    singleOf(::DeletePaymentUseCase)
    singleOf(::EditPaymentUseCase)
    singleOf(::FetchWholesaleCustomerSalesUseCase)
    singleOf(::AddWholeSaleCustomerUseCase)
    singleOf(::UpdateWholesaleProductSaleUseCase)
    singleOf(::AddWholesaleProductSaleUseCase)
    singleOf(::DeleteWholeSaleCustomerUseCase)
    singleOf(::EditWholeSalesCustomerUseCase)
    singleOf(::ConvertLossesToDatedLossesUseCase)
    singleOf(::FetchDistributorLossesUseCase)
    singleOf(::AddDistributorLossUseCase)
    singleOf(::UpdateDistributorLossUseCase)
    singleOf(::AddGoldSaleUseCase)
    singleOf(::FetchGoldPaymentsByIdsUseCase)
    singleOf(::UpdateWholesaleGoldSaleUseCase)
    singleOf(::FetchIngotTransactionsUseCase)
    singleOf(::AddIngotTransactionUseCase)
    singleOf(::UpdateIngotTransactionUseCase)
    singleOf(::DeleteStoreUseCase)
    singleOf(::AddStoreUseCase)
    singleOf(::UpdateStoreUseCase)
    singleOf(::AddManagerChequeUseCase)
    singleOf(::AddSalesChequeUseCase)
    singleOf(::FetchManagerChequesUseCase)
    singleOf(::FetchSalesChequesUseCase)
    singleOf(::UpdateManagerCheckStatusUseCase)
    singleOf(::UpdateSaleCheckStatusUseCase)
    singleOf(::UpdateManagerChequeUseCase)
    singleOf(::UpdateSalesChequeUseCase)
    singleOf(::FetchStoreLossesUseCase)
    singleOf(::FetchInventoriesUseCase)
    singleOf(::FetchStoreByIdUseCase)
    singleOf(::ConvertSalesToDatedSalesUseCase)
    singleOf(::AddInventoryUseCase)
    singleOf(::UpdateInventoryUseCase)
    singleOf(::FetchUsersByRoleUseCase)
    singleOf(::FetchDistributorUseCase)
    singleOf(::ConvertIngotTransactionsToDatedUseCase)
    singleOf(::FetchAllStoreSalesUseCase)
    singleOf(::FetchAllDistributorsSalesUseCase)
    singleOf(::FetchAllWholeCustomersUseCase)
    singleOf(::FetchManagerLossesUseCase)
    singleOf(::AddManagerLossUseCase)
    singleOf(::UpdateManagerLossUseCase)
    singleOf(::DeleteManagerLossUseCase)
    singleOf(::AddManufacturerOrderUseCase)
    singleOf(::FetchManufacturerOrdersUseCase)
    singleOf(::UpdateManufacturerOrderUseCase)
    singleOf(::DeleteManufacturerOrderUseCase)
    singleOf(::FetchSuppliersUseCase)
    singleOf(::AddSupplierUseCase)
    singleOf(::UpdateSupplierUseCase)
    singleOf(::DeleteSupplierUseCase)
    singleOf(::FetchSupplierPaymentsUseCase)
    singleOf(::FetchSuppliersUseCase)
}
val repositoryModule = module {
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::SaleRepositoryImpl) { bind<SaleRepository>() }
    singleOf(::LossRepositoryImpl) { bind<LossRepository>() }
    singleOf(::StoreRepositoryImpl) { bind<StoreRepository>() }
    singleOf(::CategoryRepositoryImpl) { bind<CategoryRepository>() }
    singleOf(::WholeSalesCustomerRepositoryImpl) { bind<WholeSalesCustomerRepository>() }
    singleOf(::PaymentRepositoryImpl) { bind<PaymentRepository>() }
    singleOf(::ChequeRepositoryImpl) { bind<ChequeRepository>() }
    singleOf(::InventoryRepositoryImpl) { bind<InventoryRepository>() }
    singleOf(::ManufacturerOrderRepositoryImpl) { bind<ManufacturerOrderRepository>() }
    singleOf(::SupplierRepositoryImpl) { bind<SupplierRepository>() }
}
val viewModelModule = module {
    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::DisplayCustomersViewModel)
    viewModelOf(::AddCustomersViewModel)
    viewModelOf(::CustomerDetailsViewModel)
    viewModelOf(::IngotTransactionsViewModel)
    viewModelOf(::DisplayCustomersViewModel)
    viewModelOf(::AddCustomersViewModel)
    viewModelOf(::ProductSaleDetailsViewModel)
    viewModelOf(::GoldSaleDetailsViewModel)
    viewModelOf(::SaleDetailsViewModel)
    viewModelOf(::AddGoldSaleViewModel)
    viewModelOf(::SuppliersViewModel)
}
val remoteSourceModule = module {
    singleOf(::AuthenticationRemoteSourceImpl) { bind<AuthenticationRemoteSource>() }
    singleOf(::SaleRemoteSourceImpl) { bind<SaleRemoteSource>() }
    singleOf(::CategoryRemoteSourceImpl) { bind<CategoryRemoteSource>() }
    singleOf(::LossRemoteDataSourceImpl) { bind<LossRemoteDataSource>() }
    singleOf(::StoreRemoteDataSourceImpl) { bind<StoreRemoteDataSource>() }
    singleOf(::PaymentRemoteDataSourceImpl) { bind<PaymentRemoteDataSource>() }
    singleOf(::WholeSalesCustomerRemoteDataSourceImpl) { bind<WholeSalesCustomerRemoteDataSource>() }
    singleOf(::ChequeRemoteSourceImpl) { bind<ChequeRemoteSource>() }
    singleOf(::InventoryRemoteSourceImpl) { bind<InventoryRemoteSource>() }
    singleOf(::ManufacturerOrderRemoteSourceImpl) { bind<ManufacturerOrderRemoteSource>() }
    singleOf(::SupplierRemoteSourceImpl) { bind<SupplierRemoteSource>() }
    single<FirebaseFirestore> {
        val db = Firebase.firestore
        val settings = FirebaseFirestoreSettings.Builder().setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED).build()
            ).build()
        db.firestoreSettings = settings
        db
    }
    single<FirebaseCrashlytics> { Firebase.crashlytics }
}