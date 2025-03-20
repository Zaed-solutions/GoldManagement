package com.zaed.manager.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.addGoldSale.AddGoldSaleScreen
import com.zaed.common.ui.addcustomers.AddCustomersScreen
import com.zaed.common.ui.addpurchase.AddPurchaseScreen
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.common.ui.customerdetails.CustomerDetailsScreen
import com.zaed.common.ui.displaycustomers.DisplayCustomersScreen
import com.zaed.common.ui.ingottransactions.IngotTransactionsScreen
import com.zaed.common.ui.purchaseDetails.PurchaseDetailsScreen
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsScreen
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsScreen
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsScreen
import com.zaed.common.ui.supplierdetails.SupplierDetailsScreen
import com.zaed.common.ui.suppliers.SuppliersScreen
import com.zaed.manager.ui.categories.CategoriesScreen
import com.zaed.manager.ui.distributordetails.DistributorDetailsScreen
import com.zaed.manager.ui.distributors.DistributorsScreen
import com.zaed.manager.ui.distributorssales.DistributorsSalesScreen
import com.zaed.manager.ui.losses.LossesScreen
import com.zaed.manager.ui.manufacturerorders.ManufacturerOrdersScreen
import com.zaed.manager.ui.salescheques.SalesChequesScreen
import com.zaed.manager.ui.storedetails.StoreDetailsScreen
import com.zaed.manager.ui.stores.StoresScreen
import com.zaed.manager.ui.storessales.StoresSalesScreen
import com.zaed.manager.ui.transactions.TransactionsScreen
import com.zaed.manager.ui.usermanagement.UserManagementScreen

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: Route,
    navController: NavHostController,
    onShowNavDrawer: () -> Unit
) {
    NavHost (
        modifier= modifier,
        navController = navController,
        startDestination =startDestination,
    ){
        composable<Route.LoginRoute> {
            LoginScreen(
                role = UserRole.MANAGER,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUpRoute)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.UserManagementRoute){
                        popUpTo(Route.LoginRoute){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.WholeSaleCustomers> {
            DisplayCustomersScreen(
                onShowNavDrawer = onShowNavDrawer,
                navigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                },
                navigateToCustomerDetails = { customerId ->
                    navController.navigate(Route.CustomerDetails(customerId))
                },
                navigateToEditCustomer = { customerId ->
                    navController.navigate(
                        Route.AddCustomers(
                            customerId = customerId
                        )
                    )
                }
            )
        }
        composable<Route.AddCustomers> {
            val customerId = it.toRoute<Route.AddCustomers>().customerId
            AddCustomersScreen(
                customerId = customerId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Route.AddPurchaseRoute> {
            val purchaseId = it.toRoute<Route.AddPurchaseRoute>().purchaseId
            AddPurchaseScreen (
                purchaseId = purchaseId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToAddSupplier = {
                    //TODO
                },
                navigateToPurchaseDetails = {
                    navController.navigate(Route.PurchaseDetailsRoute(it))
                },
                onOpenDrawer = onShowNavDrawer
            )
        }
        composable<Route.PurchaseDetailsRoute> {
            val purchaseId = it.toRoute<Route.PurchaseDetailsRoute>().purchaseId
            PurchaseDetailsScreen(
                purchaseId = purchaseId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditPurchase = {
                    navController.navigate(Route.AddPurchaseRoute(it))
                },
                onNavigateToSupplierDetails = {
//                    navController.navigate(Route.SupplierDetailsRoute(it))

                }
            )
        }
        composable<Route.CustomerDetails> {
            val customerId = it.toRoute<Route.CustomerDetails>().customerId
            CustomerDetailsScreen(
                customerId = customerId,
                onNavigateToProductSaleDetails = {
                    navController.navigate(Route.ProductSaleDetailsRoute(it))
                },
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                },
                onBack = {
                    navController.popBackStack()
                },
                navigateToEditCustomer = { id ->
                    navController.navigate(Route.AddCustomers(id))
                },
//                onNavigateToAddGoldSale = {
//                    navController.navigate(Route.AddGoldSaleRoute(it))
//                },
//                onNavigateToAddProductSale = {
//                    navController.navigate(Route.AddProductSaleRoute(it))
//                },
            )
        }
        composable<Route.IngotTransactionsRoute> {
            IngotTransactionsScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.SignUpRoute> {
            SignUpScreen(
                role = UserRole.MANAGER,
                onBack = {
                    navController.popBackStack()
                },
                navigateToLogIn = {
                    navController.navigate(Route.LoginRoute)
                }
            )
        }
        composable<Route.UserManagementRoute> {
            UserManagementScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.StoresRoute> {
            StoresScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToStoreDetails = {
                    navController.navigate(Route.StoreDetailsRoute(it))
                }
            )
        }
        composable<Route.StoreDetailsRoute> { backstackEntry ->
            val storeId = backstackEntry.toRoute<Route.StoreDetailsRoute>().storeId
            StoreDetailsScreen(
                storeId = storeId,
                onNavigateToSaleDetails = {
                    navController.navigate(Route.StoreSaleDetailsRoute(it))
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable<Route.DistributorsRoute> {
            DistributorsScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToDistributorDetails = {
                    navController.navigate(Route.DistributorDetailsRoute(it))
                }
            )
        }
        composable<Route.DistributorDetailsRoute> {backstackEntry ->
            val distributorId = backstackEntry.toRoute<Route.DistributorDetailsRoute>().distributorId
            DistributorDetailsScreen(
                distributorId = distributorId,
                onNavigateToProductSaleDetails = {
                    navController.navigate(Route.ProductSaleDetailsRoute(it))
                },
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable<Route.StoresSalesRoute> {
            StoresSalesScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSaleDetails = {
                    navController.navigate(Route.StoreSaleDetailsRoute(it))
                }
            )
        }
        composable<Route.DistributorsSalesRoute> {
            DistributorsSalesScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToProductSaleDetails = {
                    navController.navigate(Route.ProductSaleDetailsRoute(it))
                },
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                }
            )
        }
        composable<Route.LossesRoute> {
            LossesScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.ManufacturerOrdersRoute> {
            ManufacturerOrdersScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.ChequesRoute> {
            SalesChequesScreen(
                navigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                },
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.ProductSaleDetailsRoute> {navBackStackEntry ->
            val saleId = navBackStackEntry.toRoute<Route.ProductSaleDetailsRoute>().saleId
            ProductSaleDetailsScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {
                    /*TODO*/
                },
                onNavigateToCustomerDetails = {
                    navController.navigate(Route.CustomerDetails(it))
                },
                saleId = saleId,
                isAdmin = true
            )
        }
        composable<Route.GoldSaleDetailsRoute> {navBackStackEntry ->
            val saleId = navBackStackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            GoldSaleDetailsScreen(
                onBackClicked = {
                    val previousDestination =
                        navController.previousBackStackEntry?.destination?.route?.substringBefore("?")
                    if (previousDestination == Route.AddGoldSaleRoute::class.qualifiedName) {
                        navController.navigate(Route.AddGoldSaleRoute())
                    } else {
                        navController.popBackStack()
                    }
                },
                onNavigateToEditSale = {
                    navController.navigate(Route.AddGoldSaleRoute(it))
                },
                navigateToCustomerDetails = {
                    navController.navigate(Route.CustomerDetails(it))
                },
                saleId = saleId,
                isAdmin = true
            )
        }
        composable<Route.StoreSaleDetailsRoute> {navBackStackEntry ->
            val saleId = navBackStackEntry.toRoute<Route.StoreSaleDetailsRoute>().saleId
            SaleDetailsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {
                    /*TODO*/
                },
                saleId = saleId,
                isAdmin = true
            )
        }
        composable<Route.AddGoldSaleRoute> {
            val saleId = it.toRoute<Route.AddGoldSaleRoute>().saleId
            AddGoldSaleScreen(
                onBackClicked = {navController.popBackStack()},
                saleId = saleId,
                onOpenDrawer = onShowNavDrawer,
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                }
            )
        }
        composable<Route.SuppliersRoute> {
            SuppliersScreen(
                role = UserRole.MANAGER,
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSupplierDetails = {
                    navController.navigate(Route.SupplierDetailsRoute(it))
                }
            )
        }
        composable<Route.SupplierDetailsRoute> {
            val supplierId = it.toRoute<Route.SupplierDetailsRoute>().supplierId
            SupplierDetailsScreen(
                supplierId = supplierId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onNavigateToEditPurchase = { purchaseId ->
                    navController.navigate(Route.AddPurchaseRoute(purchaseId))
                },
                onNavigateToPurchaseDetails = { purchaseId ->
                    navController.navigate(Route.PurchaseDetailsRoute(purchaseId))
                }
            )
        }
        composable<Route.TransactionsRoute> {
            TransactionsScreen(
                onNavigateToEditPurchase = {
                    navController.navigate(Route.AddPurchaseRoute(it))
                },
                onNavigateToEditSale = {
                    navController.navigate(Route.AddGoldSaleRoute(it))
                },
                onNavigateToPurchaseDetails = {
                    navController.navigate(Route.PurchaseDetailsRoute(it))
                },
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                }
            )
        }
        composable<Route.CategoriesRoute> {
            CategoriesScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
    }

}