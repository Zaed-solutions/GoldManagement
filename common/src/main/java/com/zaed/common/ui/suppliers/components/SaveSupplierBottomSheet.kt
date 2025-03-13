package com.zaed.common.ui.suppliers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.components.PhoneNumberTextField
import com.zaed.common.ui.components.TextInputTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveSupplierBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (Supplier) -> Unit,
    initialSupplier: Supplier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible
    ) {
        var supplier by remember{
            mutableStateOf(initialSupplier)
        }
        var isNameError by remember {
            mutableStateOf(false)
        }
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            dragHandle = {}
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = if(supplier.id.isBlank()) stringResource(R.string.add_supplier) else stringResource(
                        R.string.update_supplier
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = supplier.name,
                    onValueChange = {
                        supplier = supplier.copy(name = it)
                    },
                    label = stringResource(R.string.name),
                    imageVector = Icons.Default.Person,
                    isError = isNameError,
                    errorMessage = R.string.name_is_required,
                    withBorder = true
                )
                PhoneNumberTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = supplier.phone,
                    onValueChange = {
                        supplier = supplier.copy(phone = it)
                    },
                    withBorder = true
                )
                TextInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = supplier.email,
                    onValueChange = {
                        supplier = supplier.copy(email = it)
                    },
                    label = stringResource(R.string.email),
                    imageVector = Icons.Default.Mail,
                    withBorder = true
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .heightIn(min = 48.dp)
                    ,
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        if(supplier.name.isBlank()){
                            isNameError = true
                        } else {
                            onSave(supplier)
                            onDismiss()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}








