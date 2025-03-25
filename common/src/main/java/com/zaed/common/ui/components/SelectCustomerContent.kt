package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.util.formatMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomerContent(
    modifier: Modifier = Modifier,
    onAddNewCustomer: () -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit,
    selectedCustomer: WholeSaleCustomer,
    onCustomerSelected: (WholeSaleCustomer) -> Unit,
    suggestedCustomers: List<WholeSaleCustomer>,
) {
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect (selectedCustomer){
        if(selectedCustomer.id.isNotBlank()){
            expanded = false
        }
    }
    Column(
        modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            windowInsets = WindowInsets(0),
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    query = query,
                    onQueryChange = onQueryChanged,
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(R.string.search_customers)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(suggestedCustomers) { customer ->
                    Surface(
                        onClick = {
                            onCustomerSelected(customer)
                        }
                    ) {
                        ListItem(
                            headlineContent = { Text(customer.name) },
                            supportingContent = if (customer.phone.isNotBlank()) {
                                { Text(text = customer.phone, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)) }
                            } else null,
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
        AnimatedContent(selectedCustomer.id.isNotBlank()) { isCustomerSelected ->
            when {
                isCustomerSelected -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = selectedCustomer.name,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            OutlinedIconButton(
                                onClick = {
                                    onCustomerSelected(WholeSaleCustomer())
                                },
                                colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                        DetailRow(
                            modifier = Modifier.padding(top = 8.dp),
                            label = stringResource(R.string.phone_number),
                            value = selectedCustomer.phone,
                        )
                        DetailRow(
                            label = stringResource(R.string.email),
                            value = selectedCustomer.email,
                        )

                        DetailRow(
                            label = stringResource(R.string.city),
                            value = selectedCustomer.city,
                        )
                        DetailRow(
                            label = stringResource(R.string.address),
                            value = selectedCustomer.address,
                            isDividerVisible = selectedCustomer.inDebt
                        )
                        if (selectedCustomer.inDebt) {
                            DetailRow(
                                label = stringResource(R.string.debt_amount),
                                value = selectedCustomer.moneyDebtAmount.formatMoney(),
                                isDividerVisible = false
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 72.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.select_a_customer_or),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        val borderColor = MaterialTheme.colorScheme.outline
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .drawBehind {
                                    val borderSize = Stroke(
                                        width = 2.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                10f,
                                                10f
                                            ), 0f
                                        )
                                    )
                                    drawRoundRect(
                                        color = borderColor,
                                        style = borderSize,
                                        cornerRadius = CornerRadius(8.dp.toPx()) // Match button corner radius if needed
                                    )
                                },
                            border = null,
                            onClick = {
                                onAddNewCustomer()
                            }
                        ) {
                            Text(stringResource(R.string.add_new_customer))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
//    SelectCustomerContent(
//        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
//        query = "Test Query",
//    ) { }
}