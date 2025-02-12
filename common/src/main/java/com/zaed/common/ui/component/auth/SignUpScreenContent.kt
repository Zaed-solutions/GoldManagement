package com.zaed.common.ui.component.auth

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
import com.zaed.common.data.model.ui.AuthenticationUiAction
import com.zaed.common.data.model.ui.AuthenticationUiState
import com.zaed.common.data.model.ui.FieldsError
import com.zaed.common.data.model.ui.SignUpError
import com.zaed.common.ui.component.AlreadyHaveAccountTextButton
import com.zaed.common.ui.component.AnimatedLoading
import com.zaed.common.ui.component.CustomSnackbar
import com.zaed.common.ui.component.PasswordTextField
import com.zaed.common.ui.component.TextInputTextField
import com.zaed.common.ui.theme.GoldManagementTheme
import kotlinx.coroutines.delay

@Composable
fun SignUpScreenContent(
    uiState: AuthenticationUiState,
    onAction: (AuthenticationUiAction) -> Unit = {}
) {
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
                context.getString(R.string.sign_up_success),
                withDismissAction = true
            )
            delay(1000)
            onAction(AuthenticationUiAction.OnNavigateToPendingScreen)
        }
    }
    Scaffold(
        snackbarHost = {
            CustomSnackbar(snackbarHostState, uiState)
        },
        modifier = Modifier.systemBarsPadding(),
//        topBar = {
//            SignUpTopAppBar(
//                onBackClicked = { onAction(AuthenticationUiAction.OnBack) }
//            )
//        }
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
                text = stringResource(R.string.sign_up),
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
                errorMessage = uiState.fieldsError,
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
                errorMessage = uiState.fieldsError,
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