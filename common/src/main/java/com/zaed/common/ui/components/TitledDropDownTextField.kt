package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitledDropDownTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    selectedValue: String,
    options: List<String>,
    enabled: Boolean = true,
    onValueChanged: (Int) -> Unit = {},
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.background
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .widthIn(max = 400.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
        ) {
            OutlinedTextField(
                readOnly = true,
                enabled = enabled,
                value = selectedValue,
                onValueChange = {},
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = shape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option: String ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            expanded = false
                            onValueChanged(index)
                        }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <DropdownMenuItem> TitledDropDownTextField2(
    modifier: Modifier = Modifier,
    label: String = "",
    placeHolder: String = "",
    selectedValue: DropdownMenuItem,
    options: List<DropdownMenuItem>,
    onValueChanged: (DropdownMenuItem) -> Unit = {},
    isError: Boolean = false,
    errorMessageRes: Int = 0,
    withBorder: Boolean = false,
    imageVector: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue.toString(),
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = if (imageVector != null) {
                {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                    )
                }
            } else null,
            label = if(label.isBlank()) null else { { Text(text = label) } },
            placeholder = if (placeHolder.isNotBlank()) {
                {
                    Text(text = placeHolder)
                }
            } else null,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(id = errorMessageRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if(withBorder) MaterialTheme.colorScheme.outline else Color.Transparent,
                focusedBorderColor =  if(withBorder) MaterialTheme.colorScheme.outline else Color.Transparent,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEachIndexed { index, option: DropdownMenuItem ->
                DropdownMenuItem(
                    text = { Text(text = option.toString()) },
                    onClick = {
                        expanded = false
                        onValueChanged(option)
                    }
                )
            }
        }
    }
}
