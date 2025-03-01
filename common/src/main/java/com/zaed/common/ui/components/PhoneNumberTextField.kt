package com.zaed.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@Composable
fun PhoneNumberTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    supportingText: String = "",
    @StringRes
    errorMessage: Int = 0,
    isError: Boolean = false,
    withBorder: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.background,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(R.string.phone_number))
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = if (withBorder) MaterialTheme.colorScheme.outline else Color.Transparent,
            focusedBorderColor = if (withBorder) MaterialTheme.colorScheme.outline else Color.Transparent,
            unfocusedContainerColor = containerColor,
            focusedContainerColor = containerColor,
        ),
        prefix = {
            Text(
                text = "+212-",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
        },
        shape = RoundedCornerShape(32.dp),
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        supportingText =
        if (isError) {
            {
                Text(
                    text = stringResource(errorMessage),
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else if (supportingText.isNotBlank()) {
            {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        } else null
    )
}