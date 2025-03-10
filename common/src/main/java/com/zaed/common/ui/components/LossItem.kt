package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.data.model.loss.ManagerLossType
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney

@Composable
fun LossItem(
    modifier: Modifier = Modifier,
    loss: Loss,
    isDeleteEnabled: Boolean,
    onDeleteLoss: () -> Unit,
    isEditEnabled: Boolean,
    onUpdateLoss: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val (title, icon) = when{
        loss is DistributorLoss && loss.allowance -> stringResource(R.string.daily_allowance) to R.drawable.ic_allowance
        loss is ManagerLoss && loss.type == ManagerLossType.ALLOWANCE -> stringResource(R.string.daily_allowance) to R.drawable.ic_allowance
        loss is ManagerLoss && loss.type == ManagerLossType.PERSONAL_EXPENSE -> loss.reason to R.drawable.ic_person
        else -> loss.reason to R.drawable.baseline_money_off_24
    }
    val moreOptions = remember(isEditEnabled, isDeleteEnabled) {
        val list = mutableListOf<MoreDropdownItem>()
        if (isEditEnabled) {
            list.add(
                MoreDropdownItem(
                    onClick = onUpdateLoss,
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor,
                )
            )
        }
        if (isDeleteEnabled) {
            list.add(
                MoreDropdownItem(
                    onClick = onDeleteLoss,
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor,
                )
            )
        }
        list
    }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp).weight(1f),
                maxLines = 2,
                text = title.ifBlank { loss.date.format(DateFormat.SHORT_DATE_TIME) },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                )
            )
            Text(
                text = loss.value.formatMoney(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if(moreOptions.isNotEmpty()) {
                MoreDropDownMenu(
                    items = moreOptions
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    LossItem(
        modifier = Modifier.padding(vertical = 48.dp),
        loss = DistributorLoss(
            id = "1",
            value = 100.0,
            allowance = true,
            reason = "Fuel"
        ),
        onDeleteLoss = {},
        isDeleteEnabled = true,
        isEditEnabled = true,
        onUpdateLoss = {}
    )
}