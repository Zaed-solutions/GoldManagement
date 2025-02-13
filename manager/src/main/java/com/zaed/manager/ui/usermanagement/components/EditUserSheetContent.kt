package com.zaed.manager.ui.usermanagement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.zaed.common.ui.components.PasswordTextField
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.R

@Composable
fun EditUserSheetContent(
    modifier: Modifier = Modifier,
    selectedUser: User,
    onDismissRequest: () -> Unit,
    onEditUser: (User) -> Unit
) {
    var user by remember{
        mutableStateOf(selectedUser)
    }
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = user.fullName,
            onValueChange = {
                user = user.copy(fullName = it)
            },
            label = stringResource(R.string.full_name)
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = user.userName,
            onValueChange = {
                user = user.copy(userName = it)
            },
            label = stringResource(R.string.user_name)
        )
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = user.password,
            onValueChange = {
                user = user.copy(password = it)
            },
            label = stringResource(R.string.password)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEditUser(user)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}