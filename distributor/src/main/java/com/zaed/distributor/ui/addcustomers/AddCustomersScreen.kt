package com.zaed.distributor.ui.addcustomers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.Zone
import com.zaed.common.ui.auth.FieldsError
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.CustomSnackbar
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField2
import com.zaed.distributor.ui.theme.DistributorAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCustomersScreen(
    viewModel: AddCustomersViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddCustomersScreenContent(
        uiState = state,
        onAction = { action ->
            when (action) {
                AddCustomersUiAction.OnBack -> onBack()
                else -> viewModel.handleAction(action)
            }
        }
    )

}

@Composable
fun AddCustomersScreenContent(
    uiState: AddCustomersState = AddCustomersState(),
    onAction: (AddCustomersUiAction) -> Unit = {}
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarError by remember { mutableStateOf(false)}
    LaunchedEffect(uiState.error, uiState.successStatus) {
        if (uiState.error != null) {
            snackBarError = true
            snackBarHostState.showSnackbar(
                message = context.getString(uiState.error.message),
                withDismissAction = true,
            )
        }
        if (uiState.successStatus) {
            snackBarError = false
            snackBarHostState.showSnackbar(
                message = context.getString(com.zaed.common.R.string.customer_added),
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            onAction(AddCustomersUiAction.OnBack)
        }
    }
    Scaffold(
        snackbarHost = {
            CustomSnackbar(
                snackbarHostState = snackBarHostState,
                isError = snackBarError
            )
        },
        topBar = {
            AddCustomersTopBar(
                onBack = {
                    onAction(AddCustomersUiAction.OnBack)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer
                )
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.full_name),
                value = uiState.request.name,
                onValueChange = { name ->
                    onAction(AddCustomersUiAction.UpdateName(name))
                },
                isError = uiState.fieldsError in listOf(
                    FieldsError.EMPTY_FULL_NAME
                ),
                errorMessage = uiState.fieldsError.message
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.email),
                value = uiState.request.email,
                onValueChange = { email ->
                    onAction(AddCustomersUiAction.UpdateEmail(email))
                }
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.phone_number),
                value = uiState.request.phone,
                onValueChange = { phone ->
                    onAction(AddCustomersUiAction.UpdatePhone(phone))
                }
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.address),
                value = uiState.request.address,
                onValueChange = { address ->
                    onAction(AddCustomersUiAction.UpdateAddress(address))
                }
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.city),
                value = uiState.request.city,
                onValueChange = { city ->
                    onAction(AddCustomersUiAction.UpdateCity(city))
                }
            )
            TitledDropDownTextField2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = stringResource(com.zaed.common.R.string.zone),
                selectedValue = uiState.request.zone,
                options = Zone.entries,
                onValueChanged = { zone ->
                    onAction(AddCustomersUiAction.UpdateZone(zone))
                }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                onClick = {
                    onAction(AddCustomersUiAction.OnSave)
                }
            ) {
                Text(
                    text = stringResource(com.zaed.common.R.string.save),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomersTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(com.zaed.common.R.string.add_customer))
        },
        navigationIcon = {
            BackIcon {
                onBack()
            }
        }
    )
}


@Preview
@Composable
private fun AddCustomersScreenPreview() {
    DistributorAppTheme {
        AddCustomersScreenContent()
    }

}