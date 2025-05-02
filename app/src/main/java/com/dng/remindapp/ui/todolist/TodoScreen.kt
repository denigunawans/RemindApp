package com.dng.remindapp.ui.todolist

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.R
import com.dng.remindapp.model.Todo
import com.dng.remindapp.ui.TodoItem
import com.dng.remindapp.util.convertDateStringToStartOfDay
import com.dng.remindapp.util.convertMillisToDateFormat
import com.dng.remindapp.util.toStringFormat
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoScreen(
    todolist: List<Todo>,
    onEditAction: (Todo) -> Unit,
    onDeleteAction: (Todo) -> Unit,
    onDoneTask: (Todo) -> Unit,
    modifier: Modifier
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (todolist.isNotEmpty()) {
            TodoBanner(
                todolist = todolist,
                modifier = Modifier
                    .fillMaxWidth()
            )
            HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
            ListTodo(
                listTodo = todolist,
                onEditAction = { onEditAction(it) },
                onDeleteAction = { onDeleteAction(it) },
                onDoneTask = { onDoneTask(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    "No todo found"
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListTodo(
    listTodo: List<Todo>,
    onEditAction: (Todo) -> Unit,
    onDeleteAction: (Todo) -> Unit,
    onDoneTask: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    val taskCategories = stringArrayResource(R.array.list_filter_array)
    val allTodolist = listTodo.sortedBy { it.deadline }.reversed()
        .groupBy { it.deadline.toLong().convertMillisToDateFormat() }
    val filteredTodolist = mutableMapOf<String, List<Todo>>()

    taskCategories.forEach { category ->
        val tempTodoList = mutableListOf<Todo>()
        listTodo.forEach { task ->
            when (category) {
                "Done Task" -> if (task.isDone) tempTodoList.add(task)
                "Missed Task" -> if (task.deadline.toLong() < Date().toStringFormat()
                        .convertDateStringToStartOfDay() && !task.isDone
                ) tempTodoList.add(task)

                "Today Task" -> if (task.deadline.toLong()
                        .convertMillisToDateFormat() == Date().toStringFormat()
                ) tempTodoList.add(task)

                else -> {}
            }
        }
        filteredTodolist[category] = tempTodoList
    }

    Column(
        modifier = modifier
    ) {
        var selectedCategory by remember { mutableStateOf(taskCategories.first()) }
        var expandedFilterMenu by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .clickable {
                    expandedFilterMenu = !expandedFilterMenu
                }
        ) {
            Text(selectedCategory, fontSize = 12.sp)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                Modifier.height(16.dp)
            )
            DropdownMenu(
                expanded = expandedFilterMenu,
                onDismissRequest = { expandedFilterMenu = false },
                modifier = Modifier.padding(0.dp)
            ) {
                taskCategories.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(filter) },
                        contentPadding = PaddingValues(6.dp),
                        onClick = {
                            selectedCategory = filter
                            expandedFilterMenu = false
                        },
                        modifier = Modifier.padding(0.dp)
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }

        val listToShow = remember { mutableMapOf<String, List<Todo>?>() }

        when (selectedCategory) {
            "Done Task" -> {
                listToShow.clear()
                listToShow["Done Task"] = filteredTodolist["Done Task"]
            }

            "Missed Task" -> {
                listToShow.clear()
                listToShow["Missed Task"] = filteredTodolist["Missed Task"]
            }

            "Today Task" -> {
                listToShow.clear()
                listToShow["Today Task"] = filteredTodolist["Today Task"]
            }

            else -> {
                listToShow.clear()
                allTodolist.forEach { (key, todolist) ->
                    listToShow[key] = todolist
                }
            }
        }

        FilterredTodolist(
            listToShow.toMap(),
            onEditAction = { onEditAction(it) },
            onDeleteAction = { onDeleteAction(it) },
            onDoneTask = { onDoneTask(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterredTodolist(
    todolist: Map<String, List<Todo>?>?,
    onEditAction: (Todo) -> Unit,
    onDoneTask: (Todo) -> Unit,
    onDeleteAction: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        todolist?.forEach { (key, listTask) ->
            stickyHeader {
                Text(key)
            }
            listTask?.size?.let {
                items(it) { index ->
                    TodoItem(
                        task = listTask[index],
                        onEditAction = { onEditAction(listTask[index]) },
                        onDeleteAction = { onDeleteAction(listTask[index]) },
                        onDoneTask = { onDoneTask(listTask[index]) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoBanner(todolist: List<Todo>, modifier: Modifier = Modifier) {

    val todoMap = mutableMapOf<String, Int>()

    var allTaskCount = 0
    var doneTaskCount = 0
    var missedTaskCount = 0
    var todayTaskCount = 0

    todolist.forEach { todo ->
        allTaskCount += 1
        when {
            todo.isDone -> doneTaskCount += 1
            todo.deadline.toLong() < Date().toStringFormat()
                .convertDateStringToStartOfDay() -> missedTaskCount += 1

            todo.deadline.toLong() == Date().toStringFormat()
                .convertDateStringToStartOfDay() -> todayTaskCount += 1
        }
    }

    todoMap["All Task"] = allTaskCount
    todoMap["Done Task"] = doneTaskCount
    todoMap["Missed Task"] = missedTaskCount
    todoMap["Today Task"] = todayTaskCount

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(10.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            todoMap.forEach { (category, count) ->
                item {
                    Card() {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(
                                    when (category) {
                                        "All Task" -> Color.Cyan
                                        "Done Task" -> Color.Green
                                        "Missed Task" -> Color.Red
                                        "Today Task" -> Color.Yellow
                                        else -> Color.Transparent
                                    }
                                )
                                .fillMaxWidth()
                        ) {
                            Text(count.toString(), fontWeight = FontWeight.Bold)
                            Text(category)
                        }
                    }
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("${doneTaskCount * 100 / allTaskCount}%", fontWeight = FontWeight.Bold)
            Text("Task done")
        }
    }
}