package com.zaed.distributor.app.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.ui.addGoldSale.AddGoldSaleScreen
import com.zaed.common.ui.addcustomers.AddCustomersScreen
import com.zaed.common.ui.addsilversale.AddSilverSaleScreen
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.common.ui.customerdetails.CustomerDetailsScreen
import com.zaed.common.ui.displaycustomers.DisplayCustomersScreen
import com.zaed.common.ui.ingottransactions.IngotTransactionsScreen
import com.zaed.common.ui.saledetails.goldsaledetails.GoldSaleDetailsScreen
import com.zaed.common.ui.saledetails.productsaledetails.ProductSaleDetailsScreen
import com.zaed.distributor.ui.addproductsale.AddProductSaleScreen
import com.zaed.distributor.ui.losses.LossesScreen
import com.zaed.distributor.ui.sales.SalesScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Route,
    onShowNavDrawer: () -> Unit,
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.imePadding(),
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
                onShowNavDrawer = onShowNavDrawer,
                isOutstanding = false,
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
        composable<Route.OutStandingSalesRoute> {
            SalesScreen(
                onShowNavDrawer = onShowNavDrawer,
                isOutstanding = true,
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
                onNavigateToAddGoldSale = {
                    navController.navigate(Route.AddGoldSaleRoute(it))
                },
                onNavigateToAddProductSale = {
                    navController.navigate(Route.AddProductSaleRoute(it))
                },
            )
        }
        composable<Route.AddCustomers> {
            val customerId = it.toRoute<Route.AddCustomers>().customerId
            val type = it.toRoute<Route.AddCustomers>().type
            AddCustomersScreen(
                customerId = customerId,
                type = type,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Route.AddProductSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddProductSaleRoute>().saleId
            AddProductSaleScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                saleId = saleId,
                onNavigateToProductSaleDetails = {
                    navController.navigate(Route.ProductSaleDetailsRoute(it)) {
                        popUpTo(Route.SalesRoute) {
                            inclusive = false
                        }
                    }
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers()) {
                        popUpTo(Route.SalesRoute) {
                            inclusive = false
                        }
                    }
                },
                onOpenDrawer = onShowNavDrawer
            )
        }
        composable<Route.ProductSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.ProductSaleDetailsRoute>().saleId
            ProductSaleDetailsScreen(
                saleId = saleId,
                onBackClicked = {
                    val previousDestination =
                        navController.previousBackStackEntry?.destination?.route?.substringBefore("?")
                    if (previousDestination == Route.AddProductSaleRoute::class.qualifiedName) {
                        navController.navigate(Route.AddProductSaleRoute())
                    } else {
                        navController.popBackStack()
                    }
                },
                onNavigateToEditSale = {
                    navController.navigate(Route.AddProductSaleRoute(it))
                },
                onNavigateToCustomerDetails = {
                    navController.navigate(Route.CustomerDetails(it))
                }
            )
        }
        composable<Route.AddGoldSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddGoldSaleRoute>().saleId
            AddGoldSaleScreen(
                saleId = saleId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToGoldSaleDetails = {
                    navController.navigate(Route.GoldSaleDetailsRoute(it)) {
                        popUpTo<Route.SalesRoute> {
                            inclusive = false
                        }
                    }
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                },
                onOpenDrawer = onShowNavDrawer
            )
        }
        composable<Route.AddSilverSaleRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.AddSilverSaleRoute>().saleId
            AddSilverSaleScreen(
                saleId = saleId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToSilverSaleDetails = {
                    navController.navigate(Route.SilverSaleDetailsRoute(it)) {
                        popUpTo<Route.SalesRoute> {
                            inclusive = false
                        }
                    }
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers(type = CustomerType.SILVER))
                },
                onOpenDrawer = onShowNavDrawer
            )
        }
        composable<Route.GoldSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            GoldSaleDetailsScreen(
                saleId = saleId,
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
                }
            )
        }
        composable<Route.SilverSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.SilverSaleDetailsRoute>().saleId
            ProductSaleDetailsScreen(
                saleId = saleId,
                onBackClicked = {
                    val previousDestination =
                        navController.previousBackStackEntry?.destination?.route?.substringBefore("?")
                    if (previousDestination == Route.AddSilverSaleRoute::class.qualifiedName) {
                        navController.navigate(Route.AddSilverSaleRoute())
                    } else {
                        navController.popBackStack()
                    }
                },
                onNavigateToEditSale = {
                    navController.navigate(Route.AddSilverSaleRoute(it))
                },
                onNavigateToCustomerDetails = {
                    navController.navigate(Route.CustomerDetails(it))
                }
            )
        }
        composable<Route.LossesRoute> {
            LossesScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.IngotTransactionsRoute> {
            IngotTransactionsScreen(
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
    data object OutStandingSalesRoute : Route

    @Serializable
    data class AddProductSaleRoute(val saleId: String = "") : Route

    @Serializable
    data class AddGoldSaleRoute(val saleId: String = "") : Route

    @Serializable
    data class AddSilverSaleRoute(val saleId: String = "") : Route

    @Serializable
    data class ProductSaleDetailsRoute(val saleId: String = "") : Route

    @Serializable
    data object WholeSaleCustomers : Route

    @Serializable
    data class AddCustomers(val customerId: String = "", val type: CustomerType = CustomerType.GOLD) : Route

    @Serializable
    data class CustomerDetails(val customerId: String) : Route

    @Serializable
    data class GoldSaleDetailsRoute(val saleId: String = "") : Route

    @Serializable
    data class SilverSaleDetailsRoute(val saleId: String = "") : Route

    @Serializable
    data object LossesRoute : Route

    @Serializable
    data object IngotTransactionsRoute : Route
}