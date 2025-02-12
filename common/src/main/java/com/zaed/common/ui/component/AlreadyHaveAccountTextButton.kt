package com.zaed.common.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R


@Composable
fun AlreadyHaveAccountTextButton(
    modifier: Modifier,
    sectionOne: String = stringResource(R.string.already_have_an_account),
    sectionTwo: String = stringResource(R.string.sign_in),
    onClick : () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = sectionOne,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = sectionTwo
            )
        }
    }
}