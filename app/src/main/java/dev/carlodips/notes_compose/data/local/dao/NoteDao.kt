package dev.carlodips.notes_compose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.carlodips.notes_compose.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM Note WHERE noteId = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Query("SELECT * FROM Note")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("UPDATE Note SET isArchived = :isArchived WHERE noteId = :noteId")
    suspend fun setArchivedNote(
        noteId: Int,
        isArchived: Boolean
    )

    @Query("UPDATE Note SET isLocked = :isLocked WHERE noteId = :noteId")
    suspend fun setLockedNote(
        noteId: Int,
        isLocked: Boolean
    )
}
