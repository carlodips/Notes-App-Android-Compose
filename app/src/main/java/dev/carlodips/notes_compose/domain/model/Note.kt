package dev.carlodips.notes_compose.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    val noteId: Int,

    @ColumnInfo(name = "noteTitle")
    val noteTitle: String,

    @ColumnInfo(name = "noteBody")
    val noteBody: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "dateUpdated")
    val dateUpdated: LocalDateTime
) {
    val formattedDateUpdated: String
        get() = dateUpdated.format(DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss"))

    val displayNoteTitle: String
        get() = noteTitle.ifBlank {
            noteBody.split("\n").first()
        }

    val displayNoteBody: String
        get() = if (noteTitle.isBlank() || noteBody.isBlank()) {
            ""
        } else {
            noteBody
        }
}
