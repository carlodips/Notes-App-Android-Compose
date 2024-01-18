package dev.carlodips.notes_compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.carlodips.notes_compose.data.local.dao.NotesDao
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.utils.DateTimeConverter

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(DateTimeConverter::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract val dao: NotesDao

    companion object {
        const val DATABASE_NAME = "notes_room.db"
    }
}
