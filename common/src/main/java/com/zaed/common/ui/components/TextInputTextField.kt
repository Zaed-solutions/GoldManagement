package com.zaed.common.ui.components

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
import com.zaed.common.ui.auth.FieldsError

@Composable
fun TextInputTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeHolder: String = "",
    imageVector: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    errorMessage: FieldsError = FieldsError.NONE,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = if(label.isBlank()) null else { { Text(text = label) } },
        placeholder = if (placeHolder.isNotBlank()) {
            {
                Text(text = placeHolder)
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedContainerColor = containerColor,
            focusedContainerColor = containerColor,
        ),
        leadingIcon = if (imageVector != null) {
            {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                )
            }
        } else null,
        shape = RoundedCornerShape(32.dp),
        isError = isError,
        singleLine = true,
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