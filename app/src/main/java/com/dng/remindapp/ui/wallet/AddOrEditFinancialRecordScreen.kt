package com.dng.remindapp.ui.wallet

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.model.RecordCategory
import com.dng.remindapp.ui.DatePickerModal
import com.dng.remindapp.util.convertMillisToDateFormat
import kotlinx.coroutines.launch
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditFinancialRecordScreen(
    onAddRecord: (FinancialRecord) -> Unit,
    onUpdateRecord: (FinancialRecord) -> Unit,
    onAddCategory: (RecordCategory) -> Unit,
    onCancel: () -> Unit,
    record: FinancialRecord? = null,
    listCategory: List<RecordCategory>,
    modifier: Modifier
) {

    var iDesc by remember { mutableStateOf(record?.desc ?: "") }
    var iCategory by remember { mutableStateOf(record?.category ?: "") }
    var iNomial by remember { mutableIntStateOf(record?.nominal ?: 0) }
    var iDate by remember {
        mutableStateOf(
            record?.date ?: Instant.now().toEpochMilli().convertMillisToDateFormat()
        )
    }
    var isIncome by remember { mutableStateOf(record?.isIncome ?: false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var expandedDropDown by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            setCloseDialog = { showAddCategoryDialog = it }) {
            onAddCategory(
                RecordCategory(category = it)
            )
            showAddCategoryDialog = false
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            TopAppBar(
                title = { Text("Add financial record") },
                navigationIcon = {
                    IconButton(
                        onClick = { onCancel() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val recordTmp = FinancialRecord(
                                id = record?.id ?: 0,
                                date = iDate,
                                nominal = iNomial,
                                category = iCategory,
                                isIncome = isIncome,
                                desc = iDesc
                            )

                            val (isValid, message) = recordTmp.checkRecordIsValid()

                            scope.launch {
                                when {
                                    isValid -> {
                                        if (record != null) onUpdateRecord(recordTmp) else onAddRecord(
                                            recordTmp
                                        )
                                        snackbarState.showSnackbar(message)
                                    }

                                    !isValid -> {
                                        snackbarState.showSnackbar(message)
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Done, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TypeRecordButton(
                isIncome = isIncome,
                isIncomeClick = {
                    isIncome = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = iDate,
                onValueChange = { iDate = it.toLong().convertMillisToDateFormat() },
                readOnly = true,
                label = { Text("Select Date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Selected date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = iCategory,
                    onValueChange = { iCategory = it },
                    readOnly = true,
                    placeholder = { Text("Category") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (expandedDropDown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select category",
                            modifier = Modifier.clickable { expandedDropDown = !expandedDropDown })
                    },
                    label = { Text("Category") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        showAddCategoryDialog = true
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "add new category")
                }
            }

            DropdownMenu(
                expanded = expandedDropDown,
                onDismissRequest = { expandedDropDown = false }
            ) {
                listCategory.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.category) },
                        onClick = {
                            iCategory = category.category
                            expandedDropDown = false
                        }
                    )
                }
            }

            OutlinedTextField(
                value = iNomial.toString(),
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        iNomial = it.toInt()
                    }
                },
                label = { Text("Input nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = iDesc,
                onValueChange = { iDesc = it },
                label = { Text("Input description") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        if (it != null) {
                            iDate = it.convertMillisToDateFormat()
                        }
                    }
                ) {
                    showDatePicker = false
                }
            }
        }
    }
}

@Composable
fun TypeRecordButton(
    isIncome: Boolean,
    isIncomeClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .background(if (isIncome) Color.Green else Color.LightGray)
                .clickable {
                    isIncomeClick(true)
                }) {
            Text(
                "Income", style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .background(if (!isIncome) Color.Red else Color.LightGray)
                .clickable {
                    isIncomeClick(false)
                }) {
            Text(
                "Outcome", style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }
    }
}

fun FinancialRecord.checkRecordIsValid(): Pair<Boolean, String> {
    return when {
        this.category.isBlank() -> Pair(false, "Select category")
        this.desc.isBlank() -> Pair(false, "Input description")
        this.nominal < 1 -> Pair(false, "Input nominal")
        else -> Pair(true, "Success")
    }
}