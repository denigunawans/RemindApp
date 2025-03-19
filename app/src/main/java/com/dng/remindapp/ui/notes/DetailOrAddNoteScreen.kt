package com.dng.remindapp.ui.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.model.Note
import com.dng.remindapp.util.convertToFullDateFormat
import java.util.Date


@Composable
fun DetailOrAddNoteScreen(
    note: Note? = null,
    onInsert: (Note) -> Unit,
    onUpdate: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier
) {
    var isDetail by remember { mutableStateOf(note != null) }

    var titleInput by remember { mutableStateOf(note?.title ?: "") }
    var contentInput by remember { mutableStateOf(note?.content ?: "") }
    val dateFormat by remember { mutableLongStateOf(note?.lastModified?.toLong() ?: Date().time) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { if (!isDetail) Text("Add new note") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isDetail) {
                            isDetail = false
                        }
                        onCancel()
                    }) {
                        Icon(
                            imageVector = if (isDetail) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (note != null) {
                        IconButton(onClick = {
                            onDelete(note)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete note"
                            )
                        }
                    }

                    IconButton(onClick = {
                        when (isDetail) {
                            true -> isDetail = false
                            false -> {
                                if (note != null) {
                                    onUpdate(
                                        Note(
                                            id = note.id,
                                            title = titleInput,
                                            content = contentInput,
                                            lastModified = dateFormat.toString()
                                        )
                                    )
                                    isDetail = true
                                } else {
                                    onInsert(
                                        Note(
                                            title = titleInput,
                                            content = contentInput,
                                            lastModified = dateFormat.toString()
                                        )
                                    )
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (isDetail) Icons.Filled.Edit else Icons.Filled.Done,
                            contentDescription = null
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 1.dp
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Last modified : ${dateFormat.convertToFullDateFormat()}",
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 12.sp
                ), modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                singleLine = true,
                label = { if (!isDetail) Text("Title") },
                readOnly = isDetail,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = contentInput,
                onValueChange = { contentInput = it },
                label = { if (!isDetail) Text("Content") },
                readOnly = isDetail,
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

        }
    }
}

