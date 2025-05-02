package com.dng.remindapp.ui.wallet

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
//    var iNomial by remember { mutableIntStateOf(record?.nominal ?: 0) }
    var iNomial by remember { mutableStateOf(
        if (record != null){
            "Rp. ${record.nominal}"
        }else{
            "Rp. "
        })
    }
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
                                nominal = if (iNomial.replace("[^0-9]".toRegex(), "").isNotBlank()){
                                    iNomial.replace("[^0-9]".toRegex(), "").toInt()
                                }else{
                                    0
                                },
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
            TypeRecordButton(modifier = Modifier.fillMaxWidth(0.7f)) {
                isIncome = it
            }

            TextField(
                value = iNomial,
                onValueChange = {
                    iNomial = it
//                    iNomial = if (it.substring(4, it.length-1).isDigitsOnly() && it.isNotEmpty()) {
//                        it.substring(4, it.length-1).toIntOrNull() ?: 0
//                    } else {
//                        0
//                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 30.dp)
            )

            Row {
                FilledTonalButton(onClick = {
                    expandedDropDown = !expandedDropDown
                }) {
                    Text("Category")
                }
                FilledTonalIconButton(modifier = Modifier.padding(start = 4.dp), onClick = {
                    showAddCategoryDialog = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "add new category")
                }
            }

            TextField(
                value = iDate,
                onValueChange = { iDate = it.toLong().convertMillisToDateFormat() },
                readOnly = true,
                placeholder = { Text("Select Date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Selected date"
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(50)
                    )
            )

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

            TextField(
                value = iDesc,
                onValueChange = { iDesc = it },
                placeholder = { Text("Add description...") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(50)
                    )
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

fun FinancialRecord.checkRecordIsValid(): Pair<Boolean, String> {
    return when {
        this.nominal < 1 -> Pair(false, "Input nominal")
        this.category.isBlank() -> Pair(false, "Select category")
        this.desc.isBlank() -> Pair(false, "Input description")
        else -> Pair(true, "Success")
    }
}