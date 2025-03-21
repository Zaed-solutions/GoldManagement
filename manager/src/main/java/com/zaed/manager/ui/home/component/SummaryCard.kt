package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun SummaryCard(
    title: String,
    value: Double,
    isTrending: Boolean,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Text(
                text = value.toMoneyFormat(2),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = if (isTrending) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
//                    contentDescription = if (isTrending) "Trending Up" else "Trending Down",
//                    tint = Color.White
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                Text(
//                    text = "${changePercentage.absoluteValue}%",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White
//                )
//            }
        }
    }
}