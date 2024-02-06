package dev.carlodips.notes_compose.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.carlodips.notes_compose.domain.model.Note
import java.time.LocalDateTime

@Entity(
    tableName = "Note",
    foreignKeys = [ForeignKey(
        entity = FolderEntity::class,
        parentColumns = ["folderId"],
        childColumns = ["folderId"]
    )]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    val noteId: Int? = null,

    @ColumnInfo(name = "noteTitle")
    val noteTitle: String,

    @ColumnInfo(name = "noteBody")
    val noteBody: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "dateUpdated")
    val dateUpdated: LocalDateTime,

    @ColumnInfo(name = "isHidden")
    val isNoteHidden: Boolean = false,

    @ColumnInfo(name = "isLocked")
    val isNoteLocked: Boolean = false,

    @ColumnInfo(name = "folderId")
    val folderId: Int? = null
) {

    companion object {
        fun toEntity(note: Note) = NoteEntity(
            noteId = note.noteId,
            noteTitle = note.noteTitle,
            noteBody = note.noteBody,
            dateAdded = note.dateAdded,
            dateUpdated = note.dateUpdated,
            isNoteHidden = note.isNoteHidden,
            isNoteLocked = note.isNoteLocked,
            folderId = note.folderId
        )
    }

    fun toDomain() = Note(
        noteId = noteId,
        noteTitle = noteTitle,
        noteBody = noteBody,
        dateAdded = dateAdded,
        dateUpdated = dateUpdated,
        isNoteHidden = isNoteHidden,
        isNoteLocked = isNoteLocked,
        folderId = folderId
    )
}
