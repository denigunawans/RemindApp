package com.dng.remindapp.ui.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.R
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.ui.WalletBalance
import com.dng.remindapp.viewmodel.FinancialRecordViewModel

@Composable
fun WalletScreen(
    recordViewModel: FinancialRecordViewModel,
    onItemClicked: (FinancialRecord) -> Unit,
    onDeleteRecord: (FinancialRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(
                Brush.linearGradient(
                    listOf(
                        colorResource(R.color.extra_dark_gray),
                        colorResource(R.color.navy)
                    )
                )
            )
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        WalletBalance(
            totalIncome = recordViewModel.totalIncome.value,
            totalOutcome = recordViewModel.totalOutcome.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 30.dp)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.DarkGray,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.DarkGray
        )
    ) {
        Text(
            "Record",
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
            modifier = Modifier.padding(top = 10.dp, start = 10.dp)
        )
        if (recordGroup.isEmpty()) {
            Text("No Records", textAlign = TextAlign.Center)
        } else {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
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