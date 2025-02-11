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
import com.zaed.common.ui.components.DetailRow
import com.zaed.manager.R
import com.zaed.manager.ui.theme.GoldManagementTheme

@Composable
fun RequestsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    requests: List<User>
) {
    AnimatedContent(isLoading) { state ->
        when {
            state -> {
                //todo: loading animation
            }

            else -> {
                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = requests,
                        key = { it.id }
                    ) { request ->
                        RequestItem(
                            modifier = Modifier.animateItem(),
                            request = request
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestItem(
    modifier: Modifier = Modifier,
    request: User
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
                    text = request.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = request.role.name,
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
                        value = request.userName
                    )
                    DetailRow(
                        label = stringResource(R.string.password),
                        value = request.password,
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
    val requests = listOf<User>()
    GoldManagementTheme {
        RequestsList(
            requests = requests
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ItemPreview() {
    val request = User()
    GoldManagementTheme {
        RequestItem(
           request = request
        )
    }
}

