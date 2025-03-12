package com.zaed.common.ui.auth.login
import android.content.res.Configuration
import android.util.Log
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
import com.zaed.common.data.model.authentication.UserApprovalStatus
import com.zaed.common.data.model.authentication.UserRole
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
fun LoginScreenContent(
    role : UserRole = UserRole.NONE,
    uiState: AuthenticationUiState,
    onAction: (AuthenticationUiAction) -> Unit = {}
) {
    LaunchedEffect(role){
        onAction(AuthenticationUiAction.OnUpdateRole(role))
        Log.d("LoginScreenContent: ", "$role")
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null && uiState.errorMessage is LoginError) {
            snackbarHostState.showSnackbar(
                context.getString(uiState.errorMessage.userMessage),
                withDismissAction = true
            )
            onAction(AuthenticationUiAction.ResetError)
        }
        if (uiState.successMessage != null) {
            when(uiState.user?.approvalStatusType){
                UserApprovalStatus.PENDING -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.your_account_is_pending_for_approval),
                        withDismissAction = true
                    )
                }
                UserApprovalStatus.APPROVED -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.login_success),
                        withDismissAction = true
                    )
                    onAction(AuthenticationUiAction.OnNavigateToHomeScreen)
                }
                UserApprovalStatus.REJECTED -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.your_account_is_rejected_by_the_manager),
                        withDismissAction = true
                    )
                    onAction(AuthenticationUiAction.ResetError)
                }
                null -> {}
            }
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
                text =  buildString {
                    append(stringResource(R.string.sign_in_as))
                    append(stringResource(role.value))
                },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                text = stringResource(R.string.enter_your_account_credentials_to_sign_in),
                style = MaterialTheme.typography.labelLarge
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
            //sign button
            Button(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { onAction(AuthenticationUiAction.OnSignIn) }
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.sign_in)
                )
            }
            Spacer(Modifier.weight(3f))
            //don_t_have_an_account
            AlreadyHaveAccountTextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onAction(AuthenticationUiAction.OnSignUp) },
                sectionOne = stringResource(R.string.don_t_have_an_account),
                sectionTwo = stringResource(R.string.sign_up)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun LoginScreenContentPreview() {
    GoldManagementTheme {
        Surface() {
            LoginScreenContent(uiState = AuthenticationUiState())
        }
    }
}