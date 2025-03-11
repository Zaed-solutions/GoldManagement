package com.zaed.manager.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsScreen
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsScreen
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsScreen
import com.zaed.manager.ui.distributordetails.DistributorDetailsScreen
import com.zaed.manager.ui.distributors.DistributorsScreen
import com.zaed.manager.ui.distributorssales.DistributorsSalesScreen
import com.zaed.manager.ui.losses.LossesScreen
import com.zaed.manager.ui.manufacturerorders.ManufacturerOrdersScreen
import com.zaed.manager.ui.storedetails.StoreDetailsScreen
import com.zaed.manager.ui.stores.StoresScreen
import com.zaed.manager.ui.storessales.StoresSalesScreen
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
                    /*TODO*/
                },
                saleId = saleId,
                isAdmin = true
            )
        }
        composable<Route.GoldSaleDetailsRoute> {navBackStackEntry ->
            val saleId = navBackStackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            GoldSaleDetailsScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {
                    /*TODO*/
                },
                navigateToCustomerDetails = {
                    /*TODO*/
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
    }

}