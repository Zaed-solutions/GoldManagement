package com.zaed.cashier.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onNavigate: (Route) -> Unit
) {
    BottomAppBar {
        BottomNavigationItem.entries
            .forEach { navigationItem ->
                val isSelected by remember(currentRoute) {
                    derivedStateOf { currentRoute == navigationItem.route::class.qualifiedName }
                }
                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(navigationItem.title),
                        )
                    },
                    onClick = {
                        onNavigate(navigationItem.route)
                    },
                )
            }
    }
}

enum class BottomNavigationItem(
    val route: Route,
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int
) {
    SALES(Route.SalesRoute, com.zaed.cashier.R.drawable.ic_money_plus, R.string.sales),
    LOSSES(Route.LossRoute, com.zaed.cashier.R.drawable.ic_money_minus, R.string.losses),
}