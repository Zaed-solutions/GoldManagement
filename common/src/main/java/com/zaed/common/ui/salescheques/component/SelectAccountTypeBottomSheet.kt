package com.zaed.common.ui.salescheques.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountTypeBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAccountTypeSelected: (isSupplier: Boolean) -> Unit,
    ) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.select_account_type),
                    style = MaterialTheme.typography.titleLarge
                )
                Surface(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        onAccountTypeSelected(false)
                    },
                    color = Color.Transparent
                ) {
                    ListItem(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        headlineContent = {
                            Text(
                                text = stringResource(R.string.customer),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null
                            )
                        }
                    )
                }
                Surface(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        onAccountTypeSelected(true)
                    },
                    color = Color.Transparent
                ) {
                    ListItem(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        headlineContent = {
                            Text(
                                text = stringResource(R.string.supplier),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}