
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.Loss
import com.zaed.common.ui.components.SwipeToRevealItem
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun LossItem(
    loss: Loss,
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {}
) {
    SwipeToRevealItem(
        secondActionIcon = Icons.Default.Edit,
        onSecondActionClicked = onClickEdit,
        onClickDelete = onClickDelete,
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = loss.value.toMoneyFormat(),
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = loss.reason,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
    }
}
