package com.zaed.manager.ui.usermanagement.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.zaed.common.data.model.User
import com.zaed.manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersTabRow(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = selectedIndex,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier
                    .run {
                        if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                            scale(-1f, 1f)
                        else
                            this
                    }
                    .tabIndicatorOffset(selectedIndex, true),
                width = Dp.Unspecified,
            )
        }
    ) {
        UserManagementTab.entries.forEach { tab ->
            Tab(
                text = { Text(text = stringResource(tab.titleRes)) },
                selected = selectedIndex == tab.ordinal,
                onClick = { onTabSelected(tab.ordinal) }
            )
        }
    }
}

enum class UserManagementTab(val titleRes: Int) {
    Users(R.string.users),
    Requests(R.string.requests),
    Rejects(R.string.rejects)
}