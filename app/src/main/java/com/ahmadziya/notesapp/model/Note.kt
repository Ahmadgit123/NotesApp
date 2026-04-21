package com.ahmadziya.notesapp.model

import com.ahmadziya.notesapp.data.NoteEntity

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val reminderTime: Long? = null
)

fun NoteEntity.toNote(): Note = Note(
    id = this.id,
    title = this.title,
    content = this.content,
    timestamp = this.timestamp,
    reminderTime = this.reminderTime
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    timestamp = this.timestamp,
    reminderTime = this.reminderTime
)
