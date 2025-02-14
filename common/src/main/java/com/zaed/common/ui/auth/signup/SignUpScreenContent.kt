package com.zaed.common.ui.auth.signup

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.components.AlreadyHaveAccountTextButton
import com.zaed.common.ui.components.AnimatedLoading
import com.zaed.common.ui.components.CustomSnackbar
import com.zaed.common.ui.components.PasswordTextField
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.auth.AuthenticationUiAction
import com.zaed.common.ui.auth.AuthenticationUiState
import com.zaed.common.ui.auth.FieldsError
import com.zaed.common.ui.theme.GoldManagementTheme

@Composable
fun SignUpScreenContent(
    role: UserRole = UserRole.NONE,
    uiState: AuthenticationUiState,
    onAction: (AuthenticationUiAction) -> Unit = {}
) {
    LaunchedEffect(role){
        onAction(AuthenticationUiAction.OnUpdateRole(role))
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null && uiState.errorMessage is SignUpError) {
            snackbarHostState.showSnackbar(
                context.getString(uiState.errorMessage.userMessage),
                withDismissAction = true
            )
            onAction(AuthenticationUiAction.ResetError)
        }
        if (uiState.successMessage != null) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.sign_up_success_wait_for_manager_approval),
                withDismissAction = true
            )
        }
    }
    Scaffold(
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState,
                uiState.errorMessage != null
            )
        },
        modifier = Modifier.systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
                .padding(it)

        ) {
            AnimatedLoading(uiState.isLoading)
            Spacer(Modifier.weight(1f))

            Text(
                text = buildString {
                    append(stringResource(R.string.sign_up_as))
                    append(stringResource(role.value))
                },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = stringResource(R.string.fill_in_your_details_to_create_your_account),
                style = MaterialTheme.typography.labelLarge
            )
            //fullName
            TextInputTextField(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                value = uiState.fullName,
                onValueChange = { onAction(AuthenticationUiAction.OnUpdateFullName(it)) },
                label = stringResource(R.string.full_name),
                imageVector = Icons.Default.Person,
                errorMessage = uiState.fieldsError.message,
                isError = uiState.fieldsError in listOf(
                    FieldsError.EMPTY_FULL_NAME,
                    FieldsError.INVALID_FULL_NAME
                )
            )
            //userName
            TextInputTextField(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                value = uiState.userName,
                onValueChange = { onAction(AuthenticationUiAction.OnUpdateUserName(it)) },
                label = stringResource(R.string.user_name),
                imageVector = Icons.Default.AlternateEmail,
                errorMessage = uiState.fieldsError.message,
                isError = uiState.fieldsError in listOf(
                    FieldsError.EMPTY_USER_NAME,
                    FieldsError.INVALID_USER_NAME
                )
            )
            //Password
            PasswordTextField(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                value = uiState.password,
                onValueChange = { onAction(AuthenticationUiAction.OnUpdatePassword(it)) },
                label = stringResource(R.string.password),
                errorMessage = uiState.fieldsError,
                isError = uiState.fieldsError in listOf(
                    FieldsError.EMPTY_PASSWORD,
                    FieldsError.INVALID_PASSWORD
                )
            )
            //signup button
            Button(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { onAction(AuthenticationUiAction.OnSignUp) }
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.sign_up)
                )
            }
            Spacer(Modifier.weight(3f))
            //Already have account
            AlreadyHaveAccountTextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onAction(AuthenticationUiAction.OnSignIn) }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SignUpScreenContentPreview() {
    GoldManagementTheme {
        Surface() {
            SignUpScreenContent(uiState = AuthenticationUiState())
        }
    }
}