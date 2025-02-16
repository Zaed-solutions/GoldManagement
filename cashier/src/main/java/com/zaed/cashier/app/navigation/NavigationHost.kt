package com.zaed.cashier.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zaed.cashier.ui.addsale.AddSaleScreen
import com.zaed.cashier.ui.loss.LossScreen
import com.zaed.cashier.ui.sales.SalesScreen
import com.zaed.cashier.ui.sales.details.SaleDetailsScreen
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Route
) {
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination =startDestination,
    ){
        composable<Route.SignUpRoute> {
            SignUpScreen(
                role = UserRole.CASHIER,
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
                role = UserRole.CASHIER,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUpRoute)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.SalesRoute){
                        popUpTo(Route.LoginRoute){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.SalesRoute> {
            SalesScreen(
                onNavigateToSaleDetails = {
                    navController.navigate(Route.SaleDetailsRoute(it))
                },
                onNavigateToAddSale = {
                    navController.navigate(Route.AddSaleRoute)
                }
            )
        }
        composable<Route.SaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.SaleDetailsRoute>().saleId
            SaleDetailsScreen(
                saleId = saleId,
                onBack = {
                    navController.popBackStack()
                },

            )
        }
        composable<Route.AddSaleRoute> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                AddSaleScreen(
                    onBackClicked = { navController.popBackStack() },
                    onNavigateToSaleDetails = { saleId ->
                        navController.navigate(Route.SaleDetailsRoute(saleId)){
                            popUpTo(Route.SalesRoute){
                                inclusive = false
                            }
                        }
                    }
                )
            }
        }

        composable<Route.Loss> {
            LossScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}






sealed interface Route{
    @Serializable
    data object SignUpRoute: Route
    @Serializable
    data object LoginRoute: Route
    @Serializable
    data object SalesRoute: Route
    @Serializable
    data object Home: Route
    @Serializable
    data object Loss: Route
    @Serializable
    data class SaleDetailsRoute(val saleId: String): Route
    @Serializable
    data object AddSaleRoute: Route
}