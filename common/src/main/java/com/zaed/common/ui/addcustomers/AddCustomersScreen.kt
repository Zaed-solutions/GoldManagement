package com.zaed.common.ui.addcustomers

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.ui.auth.FieldsError
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.CustomSnackbar
import com.zaed.common.ui.components.PhoneNumberTextField
import com.zaed.common.ui.components.TextInputTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddCustomersScreen(
    viewModel: AddCustomersViewModel = koinViewModel(),
    type: CustomerType = CustomerType.GOLD,
    customerId: String,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.init(customerId, type)
    }
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
    var snackBarError by remember { mutableStateOf(false) }
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
            Toast.makeText(
                context,
                if (uiState.isEditMode) context.getString(R.string.customer_edited) else context.getString(
                    R.string.customer_added
                ),
                Toast.LENGTH_LONG
            ).show()
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
                isEditMode = uiState.isEditMode,
                type = uiState.request.type,
                onBack = {
                    onAction(AddCustomersUiAction.OnBack)
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(0.dp),
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    shadowElevation = 8.dp
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(65.dp),
                        onClick = {
                            if (uiState.isEditMode) {
                                onAction(AddCustomersUiAction.OnEdit)
                            } else {
                                onAction(AddCustomersUiAction.OnSave)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer
                )
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(R.string.full_name),
                value = uiState.request.name,
                imageVector = Icons.Default.Person,
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
                    .fillMaxWidth(),
                label = stringResource(R.string.email),
                value = uiState.request.email,
                imageVector = Icons.Default.Mail,
                onValueChange = { email ->
                    onAction(AddCustomersUiAction.UpdateEmail(email))
                }
            )
            PhoneNumberTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = uiState.request.phone,
                onValueChange = { phone ->
                    onAction(AddCustomersUiAction.UpdatePhone(phone))
                },
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(R.string.address),
                value = uiState.request.address,
                imageVector = Icons.Default.LocationOn,
                onValueChange = { address ->
                    onAction(AddCustomersUiAction.UpdateAddress(address))
                }
            )
            TextInputTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(R.string.city),
                value = uiState.request.city,
                imageVector = Icons.Default.LocationCity,
                onValueChange = { city ->
                    onAction(AddCustomersUiAction.UpdateCity(city))
                }
            )
            if (uiState.distributor.role != UserRole.DISTRIBUTOR) {
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(R.string.note),
                    value = uiState.request.note,
                    imageVector = Icons.Default.Info,
                    onValueChange = { city ->
                        onAction(AddCustomersUiAction.UpdateNote(city))
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.pay_with_cheques),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = uiState.request.payWithCheques,
                        onCheckedChange = {
                            onAction(AddCustomersUiAction.UpdatePayWithCheques(it))
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomersTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    type: CustomerType,
    isEditMode: Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = when {
                    isEditMode && type == CustomerType.GOLD -> stringResource(R.string.edit_gold_customer)
                    type == CustomerType.GOLD -> stringResource(R.string.add_gold_customer)
                    isEditMode -> stringResource(R.string.edit_silver_customer)
                    else -> stringResource(R.string.add_silver_customer)
                }
            )
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
    AddCustomersScreenContent()
}