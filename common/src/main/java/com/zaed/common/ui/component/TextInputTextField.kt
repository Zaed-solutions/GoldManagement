package com.zaed.common.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.ui.FieldsError

@Composable
fun TextInputTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    imageVector: ImageVector? = null,
    errorMessage: FieldsError = FieldsError.NONE,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
        ),
        leadingIcon = {
            if (imageVector != null)
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                )
        },
        shape = RoundedCornerShape(32.dp),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = stringResource(errorMessage.message),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}