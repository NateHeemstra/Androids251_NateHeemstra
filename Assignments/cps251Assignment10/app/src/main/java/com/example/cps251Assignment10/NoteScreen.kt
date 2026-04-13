package com.example.cps251Assignment10
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import android.graphics.drawable.Icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.outlined.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {

    val notes by viewModel.notes.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Note?>(null) }

    // Validation state
    var titleTouched by remember { mutableStateOf(false) }
    var contentTouched by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Material Notes",
                    color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingNote = null
                    title = ""
                    content = ""
                    titleTouched = false
                    contentTouched = false
                },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // ======================
            // FORM CARD
            // ======================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = if (editingNote == null) "Create New Note" else "Edit Note",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleTouched = true
                        },
                        label = { Text("Title") },
                        isError = titleTouched && title.isBlank(),
                        supportingText = {
                            if (titleTouched && title.isBlank()) {
                                Text("Title cannot be empty")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = {
                            content = it
                            contentTouched = true
                        },
                        label = { Text("Content") },
                        isError = contentTouched && content.isBlank(),
                        supportingText = {
                            if (contentTouched && content.isBlank()) {
                                Text("Content cannot be empty")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(
                            onClick = {
                                if (title.isNotBlank() && content.isNotBlank()) {
                                    if (editingNote == null) {
                                        viewModel.addNote(title, content, dateFormat.format(Date()))
                                        scope.launch { snackbarHostState.showSnackbar("Note added!") }
                                    } else {
                                        viewModel.deleteNote(editingNote!!)
                                        viewModel.addNote(title, content, dateFormat.format(Date()))
                                        editingNote = null
                                        scope.launch { snackbarHostState.showSnackbar("Note updated!") }
                                    }
                                    title = ""
                                    content = ""
                                    titleTouched = false
                                    contentTouched = false
                                }
                            },
                            enabled = title.isNotBlank() && content.isNotBlank()
                        ) {
                            Text(if (editingNote == null) "Add Note" else "Update Note")
                        }

                        if (editingNote != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = {
                                editingNote = null
                                title = ""
                                content = ""
                            }) {
                                Text("Cancel Edit")
                            }
                        }
                    }
                }
            }

            // ======================
            // NOTES LIST
            // ======================
            Text("Your Notes", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {

                items(notes.size) { idx ->
                    val note = notes[idx]
                    var expanded by remember(note.id) {mutableStateOf(true)}
                    val animatedColor by animateColorAsState(
                        targetValue = if(note.isImportant)
                        MaterialTheme.colorScheme.primaryContainer
                        else
                    MaterialTheme.colorScheme.surface,
                        animationSpec = tween(300)
                    )
                    val animatedElevation by animateDpAsState(
                        targetValue = if(note.isImportant) 8.dp else 2.dp,
                        animationSpec = tween(300)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                editingNote = note
                                title = note.title
                                content = note.content

                                // reset validation when selecting note
                                titleTouched = false
                                contentTouched = false
                            }
                            .animateContentSize(animationSpec = tween(300)),
                        tonalElevation = animatedElevation,
                        color = animatedColor,
                        shape = MaterialTheme.shapes.medium
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {AnimatedVisibility(
                            visible = expanded,
                            enter = expandVertically(animationSpec = tween(300)),
                            exit = shrinkVertically(animationSpec = tween(300))
                        ) {

                            // TEXT CONTENT
                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    note.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    note.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "Last updated: ${note.date}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }}

                            // BUTTONS
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End

                        ){
                            Row(
                                modifier= Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ){
                                IconButton(onClick = {
                                    viewModel.updateNote(
                                        note.copy(isImportant = !note.isImportant)
                                    )
                                }){
                                    Icon(
                                        imageVector = if(note.isImportant)
                                            Icons.Filled.Star
                                        else
                                            Icons.Outlined.Star,
                                        contentDescription = "Favorite",
                                        tint = if(note.isImportant)
                                            MaterialTheme.colorScheme.secondary
                                            else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }
                                Spacer(modifier = Modifier.width(8.dp))



                            IconButton(onClick = {showDeleteDialog = note}) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )


                            }
                        }}

                        }
                    }
                }
            }
        }

        // ======================
        // DELETE DIALOG
        // ======================
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteNote(showDeleteDialog!!)
                        scope.launch { snackbarHostState.showSnackbar("Note deleted!") }
                        showDeleteDialog = null
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}