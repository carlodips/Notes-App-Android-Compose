package dev.carlodips.notes_compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.carlodips.notes_compose.data.local.dao.FolderDao
import dev.carlodips.notes_compose.data.local.dao.NoteDao
import dev.carlodips.notes_compose.data.local.entity.FolderEntity
import dev.carlodips.notes_compose.data.local.entity.NoteEntity
import dev.carlodips.notes_compose.utils.DateTimeConverter

@Database(
    entities = [
        NoteEntity::class,
        FolderEntity::class
    ],
    version = 1
)
@TypeConverters(DateTimeConverter::class)
abstract class NotesAppDatabase : RoomDatabase() {

    abstract val notesDao: NoteDao

    abstract val folderDao: FolderDao

    companion object {
        const val DATABASE_NAME = "notes_room.db"
    }
}
