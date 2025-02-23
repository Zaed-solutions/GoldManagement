package com.zaed.distributor.ui.losses.components

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
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.ui.components.MultiOptionSwitch
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TextInputTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveLossBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    initialLoss: DistributorLoss,
    onSave: (DistributorLoss) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    AnimatedVisibility(isVisible) {
        var loss by remember { mutableStateOf(initialLoss) }
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_loss),
                    style = MaterialTheme.typography.titleLarge,
                )
                MultiOptionSwitch(
                    modifier = Modifier.padding(top = 16.dp),
                    options = listOf(
                        stringResource(R.string.allowance),
                        stringResource(R.string.loss),
                    ),
                    selectedIndex = if (loss.allowance) 0 else 1,
                    onOptionSelected = {
                        loss = loss.copy(allowance = it == 0)
                    }
                )
                NumberInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = loss.value,
                    onValueChange = {
                        loss = loss.copy(value = it)
                    },
                    label = stringResource(R.string.value),
                    withBorder = true,
                )
                AnimatedVisibility(!loss.allowance) {
                    TextInputTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = loss.reason,
                        onValueChange = {
                            loss = loss.copy(reason = it)
                        },
                        label = stringResource(R.string.reason),
                        withBorder = true,
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .heightIn(min = 48.dp),
                    onClick = {
                        onSave(loss)
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}

