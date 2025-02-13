package com.zaed.manager.ui.usermanagement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.ExpandableItem
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.manager.ui.theme.ManagerTheme

@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    users: List<User>,
    onEditClicked: (User) -> Unit = {},
    onRevokeAccessClicked: (User) -> Unit = {},
    onDeleteClicked: (User) -> Unit = {}
) {
    ListWithLoading(
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = users,
                key = { it.id }
            ) { user ->
                UserItem(
                    modifier = Modifier.animateItem(),
                    user = user,
                    onEditClicked = {
                        onEditClicked(user)
                    },
                    onRevokeAccessClicked = {
                        onRevokeAccessClicked(user)
                    },
                    onDeleteClicked = {
                        onDeleteClicked(user)
                    }
                )
            }
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    onEditClicked: () -> Unit = {},
    onRevokeAccessClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {}
) {
    var isOptionMenuVisible by remember {
        mutableStateOf(false)
    }
    ExpandableItem(
        modifier = modifier,
        collapsedContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(user.role.value),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    IconButton(
                        onClick = { isOptionMenuVisible = !isOptionMenuVisible },
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(24.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                    }

                    DropdownMenu(
                        expanded = isOptionMenuVisible,
                        shadowElevation = 8.dp,
                        shape = MaterialTheme.shapes.medium,
                        onDismissRequest = { isOptionMenuVisible = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onEditClicked()
                            },
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.edit),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Icon(
                                        modifier = Modifier.padding(start = 16.dp),
                                        imageVector = Icons.Default.Edit,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                onRevokeAccessClicked()
                            },
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.revoke_access),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Icon(
                                        modifier = Modifier.padding(start = 16.dp),
                                        imageVector = Icons.Default.LockPerson,
                                        tint = MaterialTheme.colorScheme.error,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                onDeleteClicked()
                            },
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.delete),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Icon(
                                        imageVector = Icons.Default.DeleteForever,
                                        tint = MaterialTheme.colorScheme.error,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
        expandedContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                DetailRow(
                    label = stringResource(R.string.username),
                    value = user.userName
                )
                DetailRow(
                    label = stringResource(R.string.password),
                    value = user.password,
                    isDividerVisible = false
                )
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ListPreview() {
    val users = listOf<User>()
    ManagerTheme {
        UsersList(
            users = users
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ItemPreview() {
    val user = User(
        fullName = "Muhammed Edrees",
        userName = "Edrees123",
        password = "123456",
        role = UserRole.CASHIER
    )
    ManagerTheme {
        UserItem(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 64.dp),
            user = user
        )
    }
}