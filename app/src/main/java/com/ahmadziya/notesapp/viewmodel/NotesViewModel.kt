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
import com.ahmadziya.notesapp.notification.ReminderManager
import com.ahmadziya.notesapp.data.NoteEntity

// AndroidViewModel — provides Application context (needed to create database)
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    // Database and DAO setup
    private val dao = NotesDatabase.getDatabase(application).noteDao()
    private val reminderManager = ReminderManager(application)

    // Flow<List<NoteEntity>> → StateFlow<List<Note>>
    // stateIn = converts Flow into a "hot stream" for Compose
    // Notes always come from Room — even after app restart!
    val notes = dao.getAllNotes()
        .map { entityList -> entityList.map { it.toNote() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Dialog UI state
    var showDialog = mutableStateOf(false)
        private set

    var editingNote = mutableStateOf<Note?>(null)
        private set

    // ── DIALOG CONTROLS ───────────────────────────────────────────────────

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

    // ── DATABASE OPERATIONS ───────────────────────────────────────────────


    fun addNote(title: String, content: String, reminderTime: Long? = null) {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            val newNote = Note(
                title = title.trim(),
                content = content.trim(),
                reminderTime = reminderTime
            )
            // Insert and get generated ID
            val generatedId = dao.insertNote(newNote.toEntity())
            
            // Schedule reminder if set
            if (reminderTime != null) {
                reminderManager.scheduleReminder(newNote.copy(id = generatedId.toInt()))
            }
        }
        closeDialog()
    }

    fun updateNote(note: Note, newTitle: String, newContent: String, newReminderTime: Long?) {
        viewModelScope.launch {
            val updatedNote = note.copy(
                title = newTitle.trim(),
                content = newContent.trim(),
                timestamp = System.currentTimeMillis(),
                reminderTime = newReminderTime
            )
            dao.updateNote(updatedNote.toEntity())
            
            // Schedule or cancel reminder
            if (newReminderTime != null) {
                reminderManager.scheduleReminder(updatedNote)
            } else {
                reminderManager.cancelReminder(note.id)
            }
        }
        closeDialog()
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note.toEntity())
            reminderManager.cancelReminder(note.id)
        }
    }

    fun getNoteCount(): Int = notes.value.size
}
