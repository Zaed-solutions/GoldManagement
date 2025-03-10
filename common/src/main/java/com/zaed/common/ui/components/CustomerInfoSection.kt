package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.util.toMoneyFormat
import kotlin.math.absoluteValue

@Composable
fun CustomerInfoSection(
    modifier: Modifier = Modifier,
    customerName: String,
    customerDebt: Double ,
    onCustomerClicked: () -> Unit
) {
    TitledSection(
        modifier = modifier,
        title = stringResource(R.string.customer)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCustomerClicked
        ) {
            Row(

            ){
                Box (
                    Modifier
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

                ){
                    Icon(
                        imageVector = Icons.Default.PermIdentity,
                        contentDescription = "Sale icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column {
                    Text(
                        text = customerName,
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                    Row(){
                        Text(
                            text = stringResource(com.zaed.common.R.string.the_balance),
                            modifier = Modifier.padding(end = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = customerDebt.absoluteValue.toMoneyFormat(2),
                            color = if(customerDebt < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    }

}

@Preview
@Composable
private fun CustomerInfoSectionPreview() {
        CustomerInfoSection(
            customerName = "Mohamed Mahmoud",
            customerDebt = 100.0
        ){}

    
}