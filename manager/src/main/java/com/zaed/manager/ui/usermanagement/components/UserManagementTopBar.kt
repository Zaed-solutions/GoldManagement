package com.zaed.manager.ui.usermanagement.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zaed.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.user_management),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}