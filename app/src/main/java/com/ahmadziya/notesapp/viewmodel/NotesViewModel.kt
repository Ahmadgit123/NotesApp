package com.ahmadziya.notesapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ahmadziya.notesapp.data.NotesDatabase
import com.ahmadziya.notesapp.model.Note
import com.ahmadziya.notesapp.model.toEntity
import com.ahmadziya.notesapp.model.toNote
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NotesDatabase.getDatabase(application).noteDao()
    private val reminderManager = com.ahmadziya.notesapp.notification.ReminderManager(application)

    // Room Flow → StateFlow<List<Note>>
    // Whenever DB changes, Compose UI automatically refreshes
    val notes = dao.getAllNotes()
        .map { list -> list.map { it.toNote() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Dialog state
    var showDialog = mutableStateOf(false)
        private set

    var editingNote = mutableStateOf<Note?>(null)
        private set

    // ── DIALOG CONTROLS ──────────────────────────────────────────────────

    fun openAddDialog() {
        editingNote.value = null
        showDialog.value = true
    }

    fun openEditDialog(note: Note) {
        editingNote.value = note
        showDialog.value = true
    }

    fun closeDialog() {
        showDialog.value = false
        editingNote.value = null
    }

    // ── CRUD OPERATIONS ──────────────────────────────────────────────────

    fun addNote(title: String, content: String, reminderTime: Long? = null) {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            try {
                val note = Note(id = 0, title = title.trim(), content = content.trim(), reminderTime = reminderTime)
                val newId = dao.insertNote(note.toEntity())
                
                if (reminderTime != null) {
                    reminderManager.scheduleReminder(note.copy(id = newId.toInt()))
                }
            } finally {
                closeDialog()
            }
        }
    }

    fun updateNote(note: Note, newTitle: String, newContent: String, newReminderTime: Long? = null) {
        android.util.Log.d("NotesViewModel", "Attempting to update note with ID: ${note.id}")
        viewModelScope.launch {
            try {
                val updatedNote = note.copy(
                    title = newTitle.trim(),
                    content = newContent.trim(),
                    timestamp = System.currentTimeMillis(),
                    reminderTime = newReminderTime
                )
                
                // Use the standard Update method which relies on the PrimaryKey (id)
                dao.updateNote(updatedNote.toEntity())
                android.util.Log.d("NotesViewModel", "Database update successful for ID: ${note.id}")
                
                if (newReminderTime != null) {
                    reminderManager.scheduleReminder(updatedNote)
                } else {
                    reminderManager.cancelReminder(note.id)
                }
            } catch (e: Exception) {
                android.util.Log.e("NotesViewModel", "Update failed for ID: ${note.id}", e)
            } finally {
                closeDialog()
            }
        }
    }

    // ✅ BUG FIX: Pass note.id directly — use deleteById instead of @Delete
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteById(note.id)
            reminderManager.cancelReminder(note.id)
        }
    }

    fun getNoteCount(): Int = notes.value.size
}