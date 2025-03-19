package com.dng.remindapp.ui.todolist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dng.remindapp.model.Todo
import com.dng.remindapp.ui.DatePickerModal
import com.dng.remindapp.util.convertDateStringToStartOfDay
import com.dng.remindapp.util.convertMillisToDateFormat
import com.dng.remindapp.util.textInputValidation
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddOrEditTodo(
    todo: Todo? = null,
    onCancel: (Boolean) -> Unit,
    onInsertTodo: (Todo) -> Unit,
    onUpdateTodo: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {

    var title by remember { mutableStateOf(todo?.title ?: "") }
    var selectedDate by remember {
        mutableLongStateOf(
            todo?.deadline?.toLong() ?: Instant.now().toEpochMilli().convertMillisToDateFormat()
                .convertDateStringToStartOfDay()
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val isValidTitle = remember(title) { title.textInputValidation() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Add todolist") },
                navigationIcon = {
                    IconButton(onClick = {
                        onCancel(false)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val newTodo = Todo(
                            title = title,
                            deadline = selectedDate.toString(),
                            isDone = false
                        )
                        when {
                            todo == null && isValidTitle -> {
                                onInsertTodo(newTodo)
                            }

                            todo != null && isValidTitle -> {
                                onUpdateTodo(
                                    Todo(
                                        id = todo.id,
                                        title = title,
                                        isDone = todo.isDone,
                                        deadline = selectedDate.toString()
                                    )
                                )
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null
                        )
                    }
                },
                backgroundColor = Color.Transparent
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                isError = !isValidTitle,
                singleLine = true,
                label = { Text("Title task") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    errorTextColor = MaterialTheme.colorScheme.error,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = selectedDate.convertMillisToDateFormat(),
                onValueChange = { selectedDate = it.toLong() },
                readOnly = true,
                label = { Text("Select date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        if (it != null) {
                            selectedDate =
                                it.convertMillisToDateFormat().convertDateStringToStartOfDay()
                        }
                    }
                ) {
                    showDatePicker = false
                }
            }
        }
    }
}
