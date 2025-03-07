package com.zaed.manager.ui.distributors.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.authentication.User
import com.zaed.common.ui.components.ListWithLoading

@Composable
fun DistributorsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    distributors: List<User>,
    onClick: (User) -> Unit
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = distributors,
                key = { it.id }
            ) { distributor ->
                DistributorItem(
                    modifier = Modifier.animateItem(),
                    distributor = distributor,
                    onClick = { onClick(distributor) }
                )
            }
        }
    }
}