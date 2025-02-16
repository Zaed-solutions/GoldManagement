package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.common.data.model.DiscountType
import com.zaed.common.ui.components.TitledDropDownTextField

@Composable
fun DiscountTypeDropDownMenu(
    modifier: Modifier = Modifier,
    selectedValue :String,
    onUpdateDiscountType: (DiscountType) -> Unit,
) {
    TitledDropDownTextField(
        modifier = modifier.padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        label = stringResource(R.string.discount),
        selectedValue = selectedValue,
        onValueChanged = {
            onUpdateDiscountType(DiscountType.entries[it])
        },
        options = DiscountType.entries.map { stringResource(it.titleRes) },
    )
}