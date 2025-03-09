package com.zaed.manager.ui.usermanagement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.authentication.UserRole

@Composable
fun UserRoleFilterRow(
    modifier: Modifier = Modifier,
    selectedRole: UserRole?,
    onFilterClicked: (UserRole) -> Unit
    ) {
    val roleFilterItems = remember{
        listOf(
            UserRole.ACCOUNTANT,
            UserRole.CASHIER,
            UserRole.DISTRIBUTOR,
            UserRole.MANAGER
        )
    }
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(roleFilterItems) { role ->
            val selected = role == selectedRole
            FilterChip(
                selected = selected,
                onClick = {
                    onFilterClicked(role)
                },
                leadingIcon = if (selected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                label = {
                    Text(
                        text = stringResource(role.value)
                    )
                }
            )
        }
    }
}