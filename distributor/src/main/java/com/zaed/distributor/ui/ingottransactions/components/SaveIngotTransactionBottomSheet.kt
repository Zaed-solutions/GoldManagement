package com.zaed.distributor.ui.ingottransactions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.Karat
import com.zaed.common.data.model.sale.TransactionType
import com.zaed.common.ui.components.MultiOptionSwitch
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveIngotTransactionBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    initialTransaction: IngotTransaction,
    onSave: (IngotTransaction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    AnimatedVisibility(isVisible) {
        var transaction by remember {
            mutableStateOf(initialTransaction)
        }
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //sale or purchase switch
                Text(
                    text = if (transaction.id.isBlank()) stringResource(R.string.add_ingot_transaction) else stringResource(
                        R.string.edit_ingot_transaction
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                MultiOptionSwitch(
                    options = TransactionType.entries.map { stringResource(it.stringRes) },
                    selectedIndex = transaction.type.ordinal,
                    onOptionSelected = { index ->
                        transaction = transaction.copy(type = TransactionType.entries[index])
                    }
                )
                //karat
                TitledDropDownTextField(
                    modifier = Modifier.fillMaxWidth(),
                    selectedValue = transaction.karat.value.toString(),
                    onValueChanged = { index ->
                        transaction = transaction.copy(karat = Karat.entries[index])
                    },
                    label = stringResource(id = R.string.karat),
                    options = Karat.entries.map { it.value.toString() },
                    shape = RoundedCornerShape(32.dp)
                )
                //grams
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = transaction.grams,
                    withBorder = true,
                    onValueChange = { grams ->
                        transaction = transaction.copy(grams = grams)
                    },
                    label = stringResource(id = R.string.grams),
                )
                //selling price
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    withBorder = true,
                    value = transaction.sellingGramPrice,
                    onValueChange = { price ->
                        transaction = transaction.copy(sellingGramPrice = price)
                    },
                    label = stringResource(id = R.string.selling_price),
                )
                //buying price
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    withBorder = true,
                    value = transaction.buyingGramPrice,
                    onValueChange = { price ->
                        transaction = transaction.copy(buyingGramPrice = price)
                    },
                    label = stringResource(id = R.string.buying_price),
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        onClick = {
                            onSave(transaction)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }

}