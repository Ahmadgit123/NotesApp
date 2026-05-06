package com.ahmadziya.notesapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // All notes — latest
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // add Note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    //  update Note - returns number of rows affected
    @Update
    suspend fun updateNote(note: NoteEntity): Int

    // Alternative update query if @Update behaves unexpectedly
    @Query("UPDATE notes SET title = :title, content = :content, timestamp = :timestamp, reminderTime = :reminderTime WHERE id = :id")
    suspend fun updateNoteFields(id: Int, title: String, content: String, timestamp: Long, reminderTime: Long?)

    // ✅ BUG FIX: @Delete object match karta hai — agar id=0 ho toh fail
    // Isliye direct SQL query use karo — ID se seedha delete
    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    // Fetch Note by ID
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?
}