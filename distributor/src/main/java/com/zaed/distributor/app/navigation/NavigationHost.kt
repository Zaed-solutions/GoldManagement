package com.zaed.distributor.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.distributor.ui.addproductsale.AddProductSaleScreen
import com.zaed.distributor.ui.sales.SalesScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Any
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Route.SignUpRoute> {
            SignUpScreen(
                role = UserRole.DISTRIBUTOR,
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
                role = UserRole.DISTRIBUTOR,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUpRoute)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.SalesRoute) {
                        popUpTo(Route.LoginRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.SalesRoute> {
            SalesScreen(
                onNavigateToLogin = {
                    navController.navigate(Route.LoginRoute) {
                        popUpTo(Route.LoginRoute) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToAddProductSale = {
                    navController.navigate(Route.AddProductSaleRoute(it))
                },
                onNavigateToProductSaleDetails = {
                    navController.navigate(Route.ProductSaleDetailsRoute(it))
                },
                onNavigateToAddGoldSale = {
                    navController.navigate(Route.AddGoldSaleRoute(it))
                },
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                }
            )
        }
        composable<Route.AddProductSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddProductSaleRoute>().saleId
            AddProductSaleScreen(
                onBackClicked = {},
                onNavigateToProductSaleDetails = {},
                onNavigateToAddCustomer = {}
            )
        }
        composable<Route.ProductSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.ProductSaleDetailsRoute>().saleId
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text("Product Sale Details $saleId")
            }  }
        composable<Route.AddGoldSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddGoldSaleRoute>().saleId
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text("Add Gold Sale $saleId")
            }
        }
        composable<Route.GoldSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text("Gold Sale Details $saleId")
            }  }

    }
}

sealed interface Route {
    @Serializable
    data object SignUpRoute : Route

    @Serializable
    data object LoginRoute : Route

    @Serializable
    data object SalesRoute : Route

    @Serializable
    data class AddProductSaleRoute(val saleId: String = "") : Route

    @Serializable
    data class AddGoldSaleRoute(val saleId: String = "") : Route

    @Serializable
    data class ProductSaleDetailsRoute(val saleId: String = "") : Route

    @Serializable
    data class GoldSaleDetailsRoute(val saleId: String = "") : Route
}