package com.dng.remindapp.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.model.Note
import com.dng.remindapp.model.Todo
import com.dng.remindapp.ui.TodoItem
import com.dng.remindapp.ui.WalletBalance
import com.dng.remindapp.ui.WalletCard
import com.dng.remindapp.ui.notes.NoteGridItem
import com.dng.remindapp.util.convertMillisToDateFormat
import com.dng.remindapp.util.toStringFormat
import com.dng.remindapp.viewmodel.FinancialRecordViewModel
import com.dng.remindapp.viewmodel.NoteViewModel
import com.dng.remindapp.viewmodel.TodolistViewModel
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    todolistViewModel: TodolistViewModel,
    noteViewModel: NoteViewModel,
    recordViewModel: FinancialRecordViewModel,
    onNavigateToTodolist: (Todo) -> Unit,
    onNavigateToNote: (Note) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        WalletCard(
            totalIncome = recordViewModel.totalIncome.value,
            totalOutcome = recordViewModel.totalOutcome.value,
            Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Text(
            "Today task",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val todayTodo = todolistViewModel.state.value.filter {
            it.deadline.toLong().convertMillisToDateFormat() == Date().toStringFormat()
        }
        if (todayTodo.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(todayTodo) { task ->
                    TodoItem(
                        task = task,
                        onEditAction = {
                            onNavigateToTodolist(it)
                        },
                        onDeleteAction = { todolistViewModel.deleteTodo(task) },
                        onDoneTask = { todolistViewModel.setTaskDone(task) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            Text("No task for today", modifier = Modifier.padding(bottom = 10.dp))
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Text(
            "Notes",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (noteViewModel.state.value.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(noteViewModel.state.value) { note ->
                    NoteGridItem(
                        note,
                        onItemClicked = {
                            onNavigateToNote(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }else{
            Text("No Notes")
        }
    }
}


