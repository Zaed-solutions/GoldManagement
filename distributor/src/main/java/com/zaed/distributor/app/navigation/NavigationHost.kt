package com.zaed.distributor.app.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.distributor.ui.addcustomers.AddCustomersScreen
import com.zaed.distributor.ui.customerdetails.CustomerDetailsScreen
import com.zaed.distributor.ui.displaycustomers.DisplayCustomersScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Any
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        modifier = Modifier.systemBarsPadding(),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Route.SignUp> {
            SignUpScreen(
                role = UserRole.DISTRIBUTOR,
                onBack = {
                    navController.popBackStack()
                },
                navigateToLogIn = {
                    navController.navigate(Route.Login)
                },
            )
        }
        composable<Route.Login> {
            LoginScreen(
                role = UserRole.DISTRIBUTOR,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUp)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.WholeSaleCustomers) {
                        popUpTo(Route.Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.Home> {
            HomeScreen()
        }
        composable<Route.WholeSaleCustomers> {
            DisplayCustomersScreen(
                navigateToAddCustomer = {
                    navController.navigate(Route.AddCustomers)
                },
                navigateToCustomerDetails = { customerId ->
                    Toast.makeText(context, customerId, Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.CustomerDetails(customerId))
                }
            )
        }
        composable<Route.CustomerDetails> {
            val customerId = it.toRoute<Route.CustomerDetails>().customerId
            CustomerDetailsScreen(customerId = customerId)
        }
        composable<Route.AddCustomers> {
            AddCustomersScreen(
                onBack = {
                    navController.popBackStack()
                }
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
    data object SignUp : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object WholeSaleCustomers : Route

    @Serializable
    data object AddCustomers : Route

    @Serializable
    data class CustomerDetails(val customerId: String) : Route


}