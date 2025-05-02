package com.dng.remindapp.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dng.remindapp.R
import com.dng.remindapp.model.Note
import com.dng.remindapp.util.convertMillisToDateFormat
import com.dng.remindapp.viewmodel.NoteViewModel

@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel,
    onItemClicked: (Note) -> Unit,
    modifier: Modifier
) {
    var isList by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            IconButton(onClick = {
                isList = false
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_grid),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                isList = true
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_list),
                    contentDescription = null
                )
            }
        }

        when {
            noteViewModel.state.value.isEmpty() -> {
                Text(text = "Note is empty")
            }

            isList -> {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(noteViewModel.state.value) { note ->
                        NoteListItem(
                            note,
                            onItemClicked = {
                                onItemClicked(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            !isList -> {
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
                                onItemClicked(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun NoteGridItem(
    note: Note,
    onItemClicked: (Note) -> Unit,
    modifier: Modifier
) {
    ElevatedCard(
        onClick = {
            onItemClicked(note)
        },
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(4.dp)
                    .background(Color.Cyan)
            )
            Text(
                text = note.title, style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(thickness = 1.dp)
            Text(
                text = note.content, style = TextStyle(
                    fontSize = 18.sp
                ),
                minLines = 5,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun NoteListItem(
    note: Note,
    onItemClicked: (Note) -> Unit,
    modifier: Modifier
) {
    ElevatedCard(
        onClick = {
            onItemClicked(note)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(4.dp)
                    .background(Color.Cyan)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)) {
                    Text(
                        text = note.title, style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = note.lastModified.toLong().convertMillisToDateFormat(),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }

                Text(
                    text = note.content, style = TextStyle(
                        fontSize = 18.sp
                    ),
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}