package com.dng.remindapp.ui.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun TypeRecordButton(modifier: Modifier = Modifier, isIncomeSelected: (Boolean) -> Unit) {
    var isIncome by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.LightGray)
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(
                        if (isIncome) Color.Green else Color.Transparent
                    )
                    .weight(1f)
                    .clickable {
                        isIncome = true
                        isIncomeSelected(true)
                    }
            ) {
                Text(
                    "INCOME", modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(
                        if (isIncome) Color.Transparent else Color.Red
                    )
                    .weight(1f)
                    .clickable {
                        isIncome = false
                        isIncomeSelected(true)
                    }
            ) {
                Text(
                    "OUTCOME", modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
fun AddCategoryDialog(
    modifier: Modifier = Modifier,
    setCloseDialog: (Boolean) -> Unit,
    addCategory: (String) -> Unit
) {
    val inputValue = remember { mutableStateOf("") }
    Dialog(onDismissRequest = { setCloseDialog(false) }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
            Text("Add new category")
            TextField(
                value = inputValue.value,
                onValueChange = { inputValue.value = it },
                placeholder = {
                    Text("New category")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    setCloseDialog(false)
                }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    addCategory(inputValue.value)
                }) {
                    Text("Save")
                }
            }
        }
    }
}