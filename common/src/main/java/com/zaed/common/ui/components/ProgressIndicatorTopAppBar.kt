package com.zaed.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressIndicatorTopAppBar(
    modifier: Modifier = Modifier,
    progress: Float,
    firstScreen: Boolean = false,
    onOpenDrawer: () -> Unit = {},
    onBackClicked: () -> Unit,
    ) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier,
        title = {
            LinearProgressIndicator(
                trackColor = MaterialTheme.colorScheme.background,
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        },
        navigationIcon = {
            if(!firstScreen)
            BackIcon {
                onBackClicked()
            }
            else {
                IconButton(
                    onClick = onOpenDrawer,
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        }
    )
}