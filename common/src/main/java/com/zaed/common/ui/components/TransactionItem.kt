package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.theme.GoldManagementTheme
import com.zaed.common.ui.theme.GoldenCustomColors
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney
import java.util.Date

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onTransactionClicked: () -> Unit,
    isDividerVisible: Boolean = true,
    isEditable: Boolean = false,
    onEdit: () -> Unit= {},
    isDeletable: Boolean = false,
    onDelete: () -> Unit = {},
) {
    val (icon, iconBackgroundColor, iconColor) = when{
        transaction is WholesaleTransaction && transaction.productType == ProductType.GOLD  -> Triple(R.drawable.ic_gold, GoldenCustomColors.current.color, GoldenCustomColors.current.onColor)
        transaction is WholesaleTransaction && transaction.productType == ProductType.INGOT  -> Triple(R.drawable.ic_ingot, GoldenCustomColors.current.color, GoldenCustomColors.current.onColor)
        else -> Triple(R.drawable.ic_shopping, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
    }
    val title = when{
        transaction is StoreTransaction -> "CR-${transaction.receiptNumber}"
        transaction is WholesaleTransaction -> "PR-${transaction.receiptNumber}"
        else -> "DR-${transaction.receiptNumber}"
    }
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val moreOptions = remember(isEditable, isDeletable) {
        val list = mutableListOf<MoreDropdownItem>()
        if (isEditable) {
            list.add(
                MoreDropdownItem(
                    onClick = onEdit,
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor ,
                )
            )
        }
        if (isDeletable) {
            list.add(
                MoreDropdownItem(
                    onClick = onDelete,
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor ,
                )
            )
        }
        list
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = { onTransactionClicked() },
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if(transaction is WholesaleTransaction && transaction.productType == ProductType.GOLD){
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = iconBackgroundColor,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = iconColor,
                                shape = CircleShape
                            )
                    )
                } else {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = iconBackgroundColor,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = transaction.createdAt.format(DateFormat.SHORT_DATE_TIME),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
                Text(
                    text = transaction.totalAmount.formatMoney(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
                if(moreOptions.isNotEmpty()){
                    MoreDropDownMenu(
                        modifier = Modifier.padding(start = 8.dp),
                        items = moreOptions
                    )
                }
            }
            if(isDividerVisible) {
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    GoldManagementTheme {
        TransactionItem(
            transaction = WholesaleTransaction(
                receiptNumber = "123456",
                createdAt = Date(),
                products = listOf(
                    Product(
                        name = "Product 1",
                        quantity = 1,
                        grams = 10.0,
                        gramPrice = 100.0
                    ),
                    Product(
                        name = "Product 2",
                        quantity = 2,
                        grams = 20.0,
                        gramPrice = 200.0
                    ),
                    Product(
                        name = "Product 3",
                        quantity = 3,
                        grams = 30.0,
                        gramPrice = 300.0
                    )
                )
            ),
            onTransactionClicked = {}
        )
    }
}