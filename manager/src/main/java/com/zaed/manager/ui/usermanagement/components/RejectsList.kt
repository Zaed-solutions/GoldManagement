package com.zaed.manager.ui.usermanagement.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.data.source.local.UserApprovementStatus
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.ExpandableItem
import com.zaed.common.R
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem

@Composable
fun RejectsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    rejects: List<User>,
    onGrantAccessClicked: (userId: String) -> Unit,
    onDeleteClicked: (user: User) -> Unit
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
                items = rejects,
                key = { it.id }
            ) { reject ->
                RejectItem(
                    modifier = Modifier.animateItem(),
                    reject = reject,
                    onGrantAccessClicked = {
                        onGrantAccessClicked(reject.id)
                    },
                    onDeleteClicked = {
                        onDeleteClicked(reject)
                    }
                )
            }
        }
    }
}

@Composable
fun RejectItem(
    modifier: Modifier = Modifier,
    reject: User,
    onGrantAccessClicked: () -> Unit = {},
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
                    text = reject.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(reject.role.value),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                MoreDropDownMenu(
                    items = listOf(
                        MoreDropdownItem(
                            title = stringResource(R.string.grant_access),
                            icon = Icons.Default.LockPerson,
                            tint = MaterialTheme.colorScheme.error,
                            onClick = onGrantAccessClicked
                        ),
                        MoreDropdownItem(
                            title = stringResource(R.string.delete),
                            icon = Icons.Default.DeleteForever,
                            tint = MaterialTheme.colorScheme.error,
                            onClick = onDeleteClicked
                        )
                    )
                )
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
                    value = reject.userName
                )
                DetailRow(
                    label = stringResource(R.string.password),
                    value = reject.password,
                    isDividerVisible = false
                )
            }
        }
    )
}