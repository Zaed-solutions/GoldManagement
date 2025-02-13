package com.zaed.manager.ui.usermanagement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RequestsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    requests: List<User>,
    onRespondToRequest: (String, Boolean) -> Unit
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
                items = requests,
                key = { it.id }
            ) { request ->
                RequestItem(
                    modifier = Modifier.animateItem(),
                    request = request,
                    onRespondToRequest = { isAccepted ->
                        onRespondToRequest(request.id, isAccepted)
                    }
                )
            }
        }
    }
}

@Composable
private fun RequestItem(
    modifier: Modifier = Modifier,
    request: User,
    onRespondToRequest: (Boolean) -> Unit
) {
    ExpandableItem(
        modifier = modifier,
        collapsedContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = request.fullName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = stringResource(request.role.value),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(
                    onClick = {
                        onRespondToRequest(false)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        onRespondToRequest(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
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
                    value = request.userName
                )
                DetailRow(
                    label = stringResource(R.string.password),
                    value = request.password,
                    isDividerVisible = false
                )
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ListPreview() {
    val requests = listOf<User>()
    ManagerTheme {
        RequestsList(
            requests = requests,
            onRespondToRequest = { _, _ -> }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ItemPreview() {
    val request = User(
        fullName = "Muhammed Edrees",
        role = UserRole.ACCOUNTANT
    )
    ManagerTheme {
        RequestItem(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
            request = request,
            onRespondToRequest = {}
        )
    }
}

