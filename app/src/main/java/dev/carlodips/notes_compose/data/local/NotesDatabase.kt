package dev.carlodips.notes_compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.carlodips.notes_compose.data.local.dao.NotesDao
import dev.carlodips.notes_compose.data.local.entity.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val dao: NotesDao

    companion object {
        const val DATABASE_NAME = "notes_room.db"
    }
}
