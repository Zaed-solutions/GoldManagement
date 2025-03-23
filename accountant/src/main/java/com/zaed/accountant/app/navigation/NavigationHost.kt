package com.zaed.accountant.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.accountant.ui.purchases.PurchasesScreen
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.addcustomers.AddCustomersScreen
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.common.ui.customerdetails.CustomerDetailsScreen
import com.zaed.common.ui.displaycustomers.DisplayCustomersScreen
import com.zaed.common.ui.purchaseDetails.PurchaseDetailsScreen
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsScreen
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsScreen
import com.zaed.common.ui.salescheques.SalesChequesScreen
import com.zaed.common.ui.supplierdetails.SupplierDetailsScreen
import com.zaed.common.ui.suppliers.SuppliersScreen

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any,
    onShowNavDrawer: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Route.SignUpRoute> {
            SignUpScreen(
                role = UserRole.ACCOUNTANT,
                onBack = {
                    navController.popBackStack()
                },
                navigateToLogIn = {
                    navController.navigate(Route.LoginRoute)
                },
            )
        }
        composable<Route.LoginRoute> {
            LoginScreen(
                role = UserRole.ACCOUNTANT,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUpRoute)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.ChequesRoute) {
                        popUpTo(Route.LoginRoute) {
                            inclusive = true
                        }
                    }
                }
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
        composable<Route.AddCustomers> {
            val customerId = it.toRoute<Route.AddCustomers>().customerId
            AddCustomersScreen(
                customerId = customerId,
                onBack = {
                    navController.popBackStack()
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
        composable<Route.ProductSaleDetailsRoute> { navBackStackEntry ->
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
            )
        }
        composable<Route.GoldSaleDetailsRoute> { navBackStackEntry ->
            val saleId = navBackStackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            GoldSaleDetailsScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {
//                    todo: navController.navigate(Route.AddGoldSaleRoute(it))
                },
                navigateToCustomerDetails = {
                    navController.navigate(Route.CustomerDetails(it))
                },
                saleId = saleId,
                isAdmin = false
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
        composable<Route.PurchaseDetailsRoute> {
            val purchaseId = it.toRoute<Route.PurchaseDetailsRoute>().purchaseId
            PurchaseDetailsScreen(
                purchaseId = purchaseId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditPurchase = {
//                    navController.navigate(Route.AddPurchaseRoute(it))
                },
                onNavigateToSupplierDetails = { supplierId ->
                    navController.navigate(Route.SupplierDetailsRoute(supplierId))
                }
            )
        }
        composable<Route.PurchasesRoute> {
            PurchasesScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToPurchaseDetails = {
                    navController.navigate(Route.PurchaseDetailsRoute(it))
                }
            )
        }
        composable<Route.SuppliersRoute> {
            SuppliersScreen(
                role = UserRole.ACCOUNTANT,
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
//                    navController.navigate(Route.AddPurchaseRoute(purchaseId))
                },
                onNavigateToPurchaseDetails = { purchaseId ->
                    navController.navigate(Route.PurchaseDetailsRoute(purchaseId))
                }
            )
        }
    }
}
