package com.ahmadziya.notesapp.model

import com.ahmadziya.notesapp.data.NoteEntity

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val reminderTime: Long? = null
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    timestamp = timestamp,
    reminderTime = reminderTime
)

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        reminderTime = reminderTime
    )
}
