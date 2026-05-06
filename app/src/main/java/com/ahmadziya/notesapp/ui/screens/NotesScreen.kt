package com.ahmadziya.notesapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmadziya.notesapp.ui.components.AddEditNoteDialog
import com.ahmadziya.notesapp.ui.components.NoteCard
import com.ahmadziya.notesapp.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {

    // collectAsState: Room Flow → Compose State (auto-refresh on DB change)
    val notes      by viewModel.notes.collectAsState()
    val showDialog by viewModel.showDialog
    val editingNote by viewModel.editingNote

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text       = "My Notes",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text  = "${notes.size} note${if (notes.size != 1) "s" else ""}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick         = { viewModel.openAddDialog() },
                containerColor  = MaterialTheme.colorScheme.secondary,
                shape           = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint               = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->

        if (notes.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier        = Modifier.fillMaxSize().padding(padding),
                contentPadding  = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(items = notes, key = { _, note -> note.id }) { index, note ->
                    AnimatedVisibility(
                        visible = true,
                        enter   = slideInVertically { it / 2 } + fadeIn()
                    ) {
                        NoteCard(
                            note     = note,
                            index    = index,
                            onEdit   = { viewModel.openEditDialog(it) },
                            onDelete = { viewModel.deleteNote(it) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddEditNoteDialog(
            editingNote = editingNote,
            onDismiss   = { viewModel.closeDialog() },
            onSave      = { title, content, reminder ->
                if (editingNote != null)
                    viewModel.updateNote(editingNote!!, title, content, reminder)
                else
                    viewModel.addNote(title, content, reminder)
            }
        )
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("📒", fontSize = 64.sp)
            Text(
                text       = "No notes yet!",
                fontSize   = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}