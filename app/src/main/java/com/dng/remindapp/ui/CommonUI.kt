package com.dng.remindapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dng.remindapp.R
import com.dng.remindapp.model.Note
import com.dng.remindapp.model.Todo
import com.dng.remindapp.model.commonmodel.DialogModel
import com.dng.remindapp.navigation.FabOptionItem
import com.dng.remindapp.util.numberToCurrencyFormat
import com.dng.remindapp.util.numberToSimpleMoneyFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun CommonConfirmDialog(
    dialogModel: DialogModel,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = {
        dialogModel.onDismiss
    }) {
        Card() {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                Icon(dialogModel.icon, contentDescription = "Dialog icon")
                Text(dialogModel.title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(dialogModel.description, maxLines = 4)
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { dialogModel.onDismiss() }
                    ) { Text("CANCEL", color = Color.Blue) }
                    TextButton(
                        onClick = { dialogModel.onConfirm() }
                    ) { Text("DELETE", color = Color.Red) }
                }
            }
        }
    }
}

@Composable
fun WalletBalance(
    totalIncome: Double,
    totalOutcome: Double,
    modifier: Modifier = Modifier
) {
    val balance = totalIncome - totalOutcome
    var showBalance by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row {
            Text(
                text = if (showBalance) numberToCurrencyFormat(balance) else "*******",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            )
            Icon(
                if (showBalance) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                tint = Color.White,
                contentDescription = "Show balance",
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    )
                    .clickable {
                        showBalance = !showBalance
                    })
        }
        Text(stringResource(R.string.wallet_card_title), color = Color.White)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(

                    top = 5.dp
                )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pendapatan", color = Color.White)
                Text(
                    stringResource(R.string.income, numberToSimpleMoneyFormat(totalIncome)),
                    style = TextStyle(
                        color = Color.Green
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pengeluaran", color = Color.White)
                Text(
                    stringResource(R.string.expenses, numberToSimpleMoneyFormat(totalOutcome)),
                    style = TextStyle(
                        color = Color.Red
                    )
                )
            }
        }
    }
}

@Composable
fun WalletCard(
    totalIncome: Double,
    totalOutcome: Double,
    modifier: Modifier = Modifier
) {
    val balance = totalIncome - totalOutcome
    Card(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(R.color.extra_dark_gray),
                            colorResource(R.color.navy)
                        )
                    )
                )
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(stringResource(R.string.wallet_card_title), color = Color.White)
            Text(
                numberToCurrencyFormat(balance),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.income, numberToSimpleMoneyFormat(totalIncome)),
                    style = TextStyle(
                        color = Color.Green
                    )
                )
                Text(
                    stringResource(R.string.expenses, numberToSimpleMoneyFormat(totalOutcome)),
                    style = TextStyle(
                        color = Color.Red
                    )
                )
            }
        }
    }
}

@Composable
fun TodoItem(
    task: Todo,
    onEditAction: (Todo) -> Unit,
    onDeleteAction: () -> Unit,
    onDoneTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = task.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onDoneTask() },
                modifier = Modifier
                    .size(14.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                    tint = if (task.isDone) Color.Green else Color.Gray,
                    contentDescription = "Done"
                )
            }
            Box {
                IconButton(
                    onClick = { dropdownExpanded = !dropdownExpanded },
                    modifier = Modifier.size(14.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_more_vert),
                        contentDescription = "More"
                    )
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            onEditAction(task)
                            dropdownExpanded = !dropdownExpanded
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDeleteAction()
                            dropdownExpanded = !dropdownExpanded
                        }
                    )
                }
            }

        }

    }
}

@Composable
fun ItemNote(note: Note, modifier: Modifier = Modifier) {
    Card(
        onClick = {

        },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.content,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun CustomFabExpendabled(
    optionItems: List<FabOptionItem>,
    fabButton: FabOptionItem = FabOptionItem(
        icon = ImageVector.vectorResource(R.drawable.ic_add),
        title = "Add"
    ),
    modifier: Modifier = Modifier,
    onItemClick: (FabOptionItem) -> Unit
) {
    var fabClicked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        // parent ui
        Column {
            // expandable ui
            AnimatedVisibility(
                visible = fabClicked,
                enter = expandVertically(tween(1500)) + fadeIn(),
                exit = shrinkVertically(tween(1200)) + fadeOut(animationSpec = tween(1000))
            ) {
                // show items
                Column(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    optionItems.forEach { optionItem ->
                        Row(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        onItemClick(optionItem)
                                        fabClicked = false
                                    }
                                )
                        ) {
                            Icon(imageVector = optionItem.icon, contentDescription = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(optionItem.title)
                        }
                    }
                }
            }

            // main fab button
            Card(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            fabClicked = !fabClicked
                        }
                    ),
                colors = CardDefaults.cardColors(Color.Magenta)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    Icon(imageVector = fabButton.icon, contentDescription = "fab")
                    AnimatedVisibility(
                        visible = fabClicked,
                        enter = expandVertically(animationSpec = tween(1500)) + fadeIn(),
                        exit = shrinkVertically(tween(1200)) + fadeOut(tween(1200))
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = fabButton.title)
                        }
                    }
                }
            }
        }
    }
}