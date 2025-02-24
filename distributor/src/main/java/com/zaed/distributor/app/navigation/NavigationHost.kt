package com.zaed.distributor.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.distributor.ui.addGoldSale.AddGoldSaleScreen
import com.zaed.distributor.ui.addcustomers.AddCustomersScreen
import com.zaed.distributor.ui.addproductsale.AddProductSaleScreen
import com.zaed.distributor.ui.customerdetails.CustomerDetailsScreen
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersScreen
import com.zaed.distributor.ui.goldsaledetails.GoldSaleDetailsScreen
import com.zaed.distributor.ui.losses.LossesScreen
import com.zaed.distributor.ui.productsaledetails.ProductSaleDetailsScreen
import com.zaed.distributor.ui.sales.SalesScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Route,
    onShowNavDrawer: () -> Unit,
    navController: NavHostController
) {
//    Log.d("CrashBugTest", "NavigationHost: reached")
    NavHost(
        modifier = Modifier.systemBarsPadding(),
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
                    ))
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
                navigateToEditCustomer = {id->
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
            AddCustomersScreen(
                customerId = customerId,
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
                    navController.navigate(Route.ProductSaleDetailsRoute(it))
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                }
            )
        }
        composable<Route.ProductSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.ProductSaleDetailsRoute>().saleId
            ProductSaleDetailsScreen(
                saleId = saleId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {
                    navController.navigate(Route.AddProductSaleRoute(it))
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
                    navController.navigate(Route.GoldSaleDetailsRoute(it))
                },
                onNavigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers())
                }
            )
        }
        composable<Route.GoldSaleDetailsRoute> { backstackEntry ->
            val saleId = backstackEntry.toRoute<Route.GoldSaleDetailsRoute>().saleId
            GoldSaleDetailsScreen(
                saleId = saleId,
                onBackClicked = {
                    navController.popBackStack()
                },
                onNavigateToEditSale = {}
            )
        }
        composable<Route.LossesRoute> {
            LossesScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge
        )
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
    data object WholeSaleCustomers : Route

    @Serializable
    data class AddCustomers(val customerId: String = "") : Route

    @Serializable
    data class CustomerDetails(val customerId: String) : Route

    @Serializable
    data class GoldSaleDetailsRoute(val saleId: String = "") : Route

    @Serializable
    data object LossesRoute: Route
}