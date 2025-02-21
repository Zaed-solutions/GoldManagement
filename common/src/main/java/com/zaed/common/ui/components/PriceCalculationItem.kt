package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.zaed.common.ui.util.formatMoney

@Composable
fun PriceCalculationItem(
    modifier: Modifier = Modifier,
    isNegative: Boolean = false,
    style: TextStyle,
    title: String,
    price: Double
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = style
        )
        Text(
            text = "${if (isNegative) "-" else ""}${price.formatMoney()}",
            style = style
        )
    }
}