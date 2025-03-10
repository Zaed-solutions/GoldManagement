package com.zaed.cashier.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.cashier.ui.addsale.AddSaleScreen
import com.zaed.cashier.ui.loss.LossScreen
import com.zaed.cashier.ui.sales.SalesScreen
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.common.ui.saledetails.cashiersaledetails.SaleDetailsScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    navController: NavHostController,
    startDestination: Route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
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
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSaleDetails = {
                    navController.navigate(Route.SaleDetailsRoute(it))
                },
                onNavigateToAddSale = {
                    navController.navigate(Route.AddSaleRoute(it))
                },
            )
        }
        composable<Route.SaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.SaleDetailsRoute>().saleId
            SaleDetailsScreen(
                saleId = saleId,
                onNavigateToEditSale = {
                    navController.navigate(Route.AddSaleRoute(it))
                },
                onBack = {
                    val previousDestination = navController.previousBackStackEntry?.destination?.route?.substringBefore("?")
                    if (previousDestination == Route.AddSaleRoute::class.qualifiedName) {
                        navController.navigate(Route.AddSaleRoute())
                    } else {
                        navController.popBackStack()
                    }
                },
            )
        }
        composable<Route.AddSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddSaleRoute>().saleId

            AddSaleScreen(
                onShowNavDrawer = onShowNavDrawer,
                saleId = saleId,
                onNavigateToSaleDetails = {
                    navController.navigate(Route.SaleDetailsRoute(it))
                }
            )
        }

        composable<Route.LossRoute> {
            LossScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }

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
    data object LossRoute : Route

    @Serializable
    data class SaleDetailsRoute(val saleId: String) : Route

    @Serializable
    data class AddSaleRoute(val saleId: String = "") : Route
}