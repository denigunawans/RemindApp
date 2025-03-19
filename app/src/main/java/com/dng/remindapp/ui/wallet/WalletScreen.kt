package com.dng.remindapp.ui.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.ui.WalletCard
import com.dng.remindapp.viewmodel.FinancialRecordViewModel

@Composable
fun WalletScreen(
    recordViewModel: FinancialRecordViewModel,
    onItemClicked: (FinancialRecord) -> Unit,
    onDeleteRecord: (FinancialRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {

        WalletCard(
            totalIncome = recordViewModel.totalIncome.value,
            totalOutcome = recordViewModel.totalOutcome.value
        )
        Text(
            "Record",
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            modifier = Modifier.padding(top = 10.dp)
        )
        ListRecord(
            recordGroup = recordViewModel.state.value.groupBy { it.date },
            onItemClicked = { onItemClicked(it) },
            onDeleteRecord = { onDeleteRecord(it) })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListRecord(
    recordGroup: Map<String, List<FinancialRecord>>,
    onItemClicked: (FinancialRecord) -> Unit,
    onDeleteRecord: (FinancialRecord) -> Unit
) {
    LazyColumn {
        recordGroup.forEach { (date, financialRecords) ->
            stickyHeader {
                Text(date, fontSize = 12.sp)
            }
            items(financialRecords) { record ->
                RecordItem(
                    onItemClicked = { onItemClicked(record) },
                    onDeleteRecord = { onDeleteRecord(record) },
                    record
                )
            }
        }
    }
}

@Composable
fun RecordItem(onItemClicked: () -> Unit, onDeleteRecord: () -> Unit, record: FinancialRecord) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp)
            .clickable {
                onItemClicked()
            }
    ) {
        Text(
            text = record.desc,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = record.nominal.toString(),
            fontSize = 12.sp,
            color = if (record.isIncome) Color.Green else Color.Red,
            modifier = Modifier
                .padding(end = if (record.isIncome) 20.dp else 0.dp)
        )
        Icon(
            Icons.Outlined.Delete,
            contentDescription = "Delete record",
            Modifier
                .padding(2.dp)
                .clickable {
                    onDeleteRecord()
                }
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 20.dp)
    )
}