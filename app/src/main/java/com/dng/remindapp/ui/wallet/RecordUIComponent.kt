package com.dng.remindapp.ui.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog

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