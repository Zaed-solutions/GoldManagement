package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

@Composable
fun ChangeLogList(
    modifier: Modifier = Modifier,
    changeLogs: List<ChangeLog>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(changeLogs) { log ->
            ChangeLogItem(log)
        }
    }
}

@Composable
fun ChangeLogItem(changeLog: ChangeLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = changeLog.employeeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "ID: ${changeLog.employeeId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = changeLog.date.format(DateFormat.DATE_TIME),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            LogTypeChip(type = changeLog.type)
        }
    }
}

@Composable
fun LogTypeChip(type: LogType) {
    val (backgroundColor, textColor) = when (type) {
        LogType.CREATE -> Color(0xFF4CAF50) to Color.White // Green
        LogType.UPDATE -> Color(0xFFFF9800) to Color.Black // Orange
        LogType.DELETE -> Color(0xFFF44336) to Color.White // Red
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = type.name,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun ChangeLogItemPreview() {
    ChangeLogList(
        changeLogs = listOf(
            ChangeLog(
                employeeId = "12345",
                employeeName = "John Doe",
                type = LogType.CREATE
            ),
            ChangeLog(
                employeeId = "54321",
                employeeName = "Jane Smith",
                type = LogType.UPDATE
            ),
            ChangeLog(
                employeeId = "98765",
                employeeName = "Bob Johnson",
                type = LogType.DELETE
            )
        )
    )
}
