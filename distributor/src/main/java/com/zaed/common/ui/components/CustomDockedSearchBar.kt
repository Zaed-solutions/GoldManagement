package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomDockedSearchBar(
    query : String,
    result: List<Pair<String,String>>,
    onQueryChange: (String) -> Unit,
    onResultSelected: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        var expanded by remember { mutableStateOf(false) }
        DockedSearchBar(
            modifier = Modifier.padding(8.dp),
            expanded = expanded,
            shape = RoundedCornerShape(8.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onSearch = {
                        expanded = false
                        onQueryChange(it)
                    },
                    expanded = expanded,
                    onQueryChange = {
                        onQueryChange(it)
                    },
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search here...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty() || expanded) {
                            IconButton(
                                onClick = {
                                    onQueryChange("")
                                    expanded = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear Icon",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }
                )
            },
            onExpandedChange = { expanded = it }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                itemsIndexed(result) { index, customer ->
                    SearchResultItem(
                        item = customer.second,
                        onItemSelected = {
                            onResultSelected(customer.first)
                        }
                    )
                }
            }
        }
    }
}