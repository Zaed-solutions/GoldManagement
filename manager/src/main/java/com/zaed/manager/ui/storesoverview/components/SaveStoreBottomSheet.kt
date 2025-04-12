package com.zaed.manager.ui.storesoverview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.components.TextInputTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveStoreBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    initialStore: Store,
    onSave: (Store) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isNameError by remember { mutableStateOf(false) }
    var isLocationError by remember { mutableStateOf(false) }
    AnimatedVisibility(isVisible) {
        var store by remember { mutableStateOf(initialStore) }
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_store),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = store.name,
                    onValueChange = {
                        store = store.copy( name = it)
                    },
                    label = stringResource(R.string.name),
                    withBorder = true,
                )
                TextInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = store.location,
                    onValueChange = {
                        store = store.copy(location = it)
                    },
                    label = stringResource(R.string.location),
                    withBorder = true,
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .heightIn(min = 48.dp),
                    onClick = {
                        if(store.name.isEmpty() && store.location.isEmpty()) {
                            isNameError = true
                            isLocationError = true
                        } else if(store.name.isEmpty()) {
                            isNameError = true
                            isLocationError = false
                        } else if(store.location.isEmpty()) {
                            isNameError = false
                            isLocationError = true
                        } else {
                            isNameError = false
                            isLocationError = false
                            onSave(store)
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }

}