package com.zaed.manager.ui.usermanagement.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.components.DetailRow
import com.zaed.manager.R
import com.zaed.manager.ui.theme.GoldManagementTheme

@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    users: List<User>
) {
    AnimatedContent(isLoading) { state ->
        when{
            state -> {
                //todo: loading animation
            }
            else -> {
                LazyColumn (
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = users,
                        key = { it.id}
                    ) { user ->
                        UserItem(
                            modifier = Modifier.animateItem(),
                            user = user
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: User
) {
    var isExpanded by remember{
        mutableStateOf(false)
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { isExpanded = !isExpanded },
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = user.role.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            AnimatedVisibility(isExpanded) {
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
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ListPreview() {
    val users = listOf<User>()
    GoldManagementTheme {
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
    GoldManagementTheme {
        UserItem(
            modifier = Modifier.padding(16.dp).padding(top = 64.dp),
            user = user
        )
    }
}