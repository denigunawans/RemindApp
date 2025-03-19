package com.dng.remindapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.model.Note
import com.dng.remindapp.model.commonmodel.DialogModel
import com.dng.remindapp.navigation.AddNote
import com.dng.remindapp.navigation.AddRecord
import com.dng.remindapp.navigation.AddTodo
import com.dng.remindapp.navigation.FabOptionItem
import com.dng.remindapp.navigation.Home
import com.dng.remindapp.navigation.MainRoute
import com.dng.remindapp.navigation.NoteRoute
import com.dng.remindapp.navigation.Todo
import com.dng.remindapp.navigation.Wallet
import com.dng.remindapp.ui.CommonConfirmDialog
import com.dng.remindapp.ui.CustomFabExpendabled
import com.dng.remindapp.ui.home.HomeScreen
import com.dng.remindapp.ui.notes.DetailOrAddNoteScreen
import com.dng.remindapp.ui.notes.NoteScreen
import com.dng.remindapp.ui.theme.RemindAppTheme
import com.dng.remindapp.ui.theme.robotoFamily
import com.dng.remindapp.ui.todolist.AddOrEditTodo
import com.dng.remindapp.ui.todolist.TodoScreen
import com.dng.remindapp.ui.wallet.AddOrEditFinancialRecordScreen
import com.dng.remindapp.ui.wallet.WalletScreen
import com.dng.remindapp.viewmodel.FinancialRecordViewModel
import com.dng.remindapp.viewmodel.NoteViewModel
import com.dng.remindapp.viewmodel.TodolistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val todolistViewModel = hiltViewModel<TodolistViewModel>()
            val notesViewModel = hiltViewModel<NoteViewModel>()
            val financialRecordViewModel = hiltViewModel<FinancialRecordViewModel>()

            RemindAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                val mainRoute = listOf(
                    MainRoute("Home", Home, ImageVector.vectorResource(R.drawable.ic_home)),
                    MainRoute("Wallet", Wallet, ImageVector.vectorResource(R.drawable.ic_wallet)),
                    MainRoute("Todo", Todo, ImageVector.vectorResource(R.drawable.ic_todo)),
                    MainRoute("Note", NoteRoute, ImageVector.vectorResource(R.drawable.ic_note))
                )
                Scaffold(
                    snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }, topBar = {
                    navBackStackEntry?.destination?.let { currentDestination ->
                        if (currentDestination.hasRoute(Home::class)) {
                            TopAppBar(
                                title = {
                                    Text(
                                        "Remind App", style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            fontFamily = robotoFamily,
                                            fontStyle = FontStyle.Italic
                                        )
                                    )
                                },
                                elevation = 0.dp,
                                backgroundColor = Color.Transparent,
                                modifier = Modifier.padding(0.dp)
                            )
                        }
                    }
                }, floatingActionButton = {
                    navBackStackEntry?.destination?.let { currentDestination ->
                        if (currentDestination.hasRoute(Home::class)) {
                            val optionItems = listOf(
                                FabOptionItem(
                                    icon = ImageVector.vectorResource(R.drawable.ic_wallet),
                                    title = "Add Record"
                                ), FabOptionItem(
                                    icon = ImageVector.vectorResource(R.drawable.ic_note),
                                    title = "Add Notes"
                                ), FabOptionItem(
                                    icon = ImageVector.vectorResource(R.drawable.ic_todo),
                                    title = "Add Todolist"
                                )
                            )

                            CustomFabExpendabled(
                                optionItems = optionItems, onItemClick = { item ->
                                    when (item.title) {
                                        "Add Record" -> navController.navigate(route = AddRecord)
                                        "Add Notes" -> navController.navigate(route = AddNote)
                                        "Add Todolist" -> {
                                            navController.navigate(route = AddTodo)
                                        }
                                    }
                                })
                        }
                    }
                }, bottomBar = {
                    val currentDestination = navBackStackEntry?.destination
                    val isMainRoute = currentDestination?.hierarchy?.any {
                        when {
                            it.hasRoute(Home::class) -> true
                            it.hasRoute(Wallet::class) -> true
                            it.hasRoute(Todo::class) -> true
                            it.hasRoute(NoteRoute::class) -> true
                            else -> false
                        }
                    }

                    if (isMainRoute == true) {
                        Card(
                            elevation = 4.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Row {
                                mainRoute.forEach { route ->
                                    val isSelected = currentDestination.hierarchy.any {
                                        it.hasRoute(route.route::class)
                                    }

                                    BottomNavigationItem(
                                        icon = {
                                        Icon(
                                            route.icon,
                                            contentDescription = route.name,
                                            tint = if (isSelected) Color.Blue else Color.DarkGray,
                                            modifier = Modifier.width(16.dp)
                                        )
                                    }, label = {
                                        Text(
                                            route.name, style = TextStyle(
                                                fontSize = 12.sp,
                                                color = if (isSelected) Color.Blue else Color.DarkGray
                                            )
                                        )
                                    }, selected = isSelected, onClick = {
                                        navController.navigate(route.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }, modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val todoToDetail =
                        remember { mutableStateOf<com.dng.remindapp.model.Todo?>(null) }
                    val noteToDetail = remember { mutableStateOf<Note?>(null) }
                    val recordToDetail = remember { mutableStateOf<FinancialRecord?>(null) }
                    val showConfirmDialog = remember { mutableStateOf(false) }
                    val dialogModel = remember { mutableStateOf(DialogModel()) }

                    if (showConfirmDialog.value) {
                        CommonConfirmDialog(
                            dialogModel = dialogModel.value,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(8.dp)
                        )
                    }

                    NavHost(navController = navController, startDestination = Home) {
                        composable<Home> {
                            HomeScreen(
                                todolistViewModel = todolistViewModel,
                                noteViewModel = notesViewModel,
                                recordViewModel = financialRecordViewModel,
                                onNavigateToTodolist = {
                                    todoToDetail.value = it
                                    navController.navigate(AddTodo)
                                },
                                onNavigateToNote = {
                                    noteToDetail.value = it
                                    navController.navigate(AddNote)
                                },
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxWidth()
                            )
                        }
                        composable<Wallet> {
                            WalletScreen(
                                recordViewModel = financialRecordViewModel,
                                onItemClicked = {
                                    navController.navigate(AddRecord)
                                    recordToDetail.value = it
                                },
                                onDeleteRecord = {
                                    dialogModel.value = DialogModel(
                                        title = "Delete record",
                                        description = "Do you want to delete record ?",
                                        icon = Icons.Outlined.Delete,
                                        onConfirm = {
                                            financialRecordViewModel.deleteRecord(it)
                                            showConfirmDialog.value = false
                                        },
                                        onDismiss = {
                                            showConfirmDialog.value = false
                                        })
                                    showConfirmDialog.value = true
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable<Todo> {
                            TodoScreen(
                                todolist = todolistViewModel.state.value,
                                onEditAction = {
                                    todoToDetail.value = it
                                    navController.navigate(AddTodo)
                                },
                                onDeleteAction = {
                                    dialogModel.value = DialogModel(
                                        title = "Delete todo",
                                        description = "Do you want to delete todo ?",
                                        icon = Icons.Outlined.Delete,
                                        onConfirm = {
                                            todolistViewModel.deleteTodo(it)
                                            showConfirmDialog.value = false
                                        },
                                        onDismiss = {
                                            showConfirmDialog.value = false
                                        })
                                    showConfirmDialog.value = true
                                },
                                onDoneTask = { todolistViewModel.setTaskDone(it) },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable<NoteRoute> {
                            NoteScreen(
                                noteViewModel = notesViewModel, onItemClicked = {
                                    navController.navigate(AddNote)
                                    noteToDetail.value = it
                                }, modifier = Modifier.fillMaxWidth()
                            )
                        }
                        composable<AddRecord> {
                            AddOrEditFinancialRecordScreen(
                                onCancel = {
                                navController.popBackStack()
                            },
                                onAddRecord = {
                                    financialRecordViewModel.insertRecord(it)
                                },
                                onUpdateRecord = {
                                    financialRecordViewModel.updateRecord(it)
                                },
                                onAddCategory = {
                                    financialRecordViewModel.insertCategory(it)
                                },
                                record = recordToDetail.value,
                                listCategory = financialRecordViewModel.categories.value,
                                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                            )
                        }
                        composable<AddTodo> {
                            AddOrEditTodo(
                                todo = todoToDetail.value, onCancel = {
                                navController.popBackStack()
                                todoToDetail.value = null
                            }, onInsertTodo = {
                                todolistViewModel.insertTodo(it)
                                navController.popBackStack()
                            }, onUpdateTodo = {
                                todolistViewModel.updateTodo(it)
                                navController.popBackStack()
                            }, modifier = Modifier.fillMaxSize()
                            )
                        }
                        composable<AddNote> {
                            DetailOrAddNoteScreen(
                                note = noteToDetail.value, onCancel = {
                                navController.popBackStack()
                                noteToDetail.value = null
                            }, onUpdate = {
                                notesViewModel.updateNote(it)
                            }, onInsert = {
                                notesViewModel.insertNote(it)
                                navController.popBackStack()
                            }, onDelete = {
                                dialogModel.value = DialogModel(
                                    title = "Delete Note",
                                    description = "Do you want to delete note ?",
                                    icon = Icons.Outlined.Delete,
                                    onConfirm = {
                                        notesViewModel.deleteNote(it)
                                        showConfirmDialog.value = false
                                    },
                                    onDismiss = {
                                        showConfirmDialog.value = false
                                    })
                                showConfirmDialog.value = true
                            }, modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}


