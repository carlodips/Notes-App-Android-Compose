package dev.carlodips.notes_compose.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Note(
    val noteId: Int? = null,

    val noteTitle: String,

    val noteBody: String,

    val dateAdded: LocalDateTime = LocalDateTime.now(),

    val dateUpdated: LocalDateTime,

    val isNoteArchived: Boolean = false,

    val isNoteLocked: Boolean = false,

    val folderId: Int? = null
) {
    val formattedDateUpdated: String // TODO: Fix formatting
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

    fun doesMatchSearchQuery(query: String): Boolean {
        return noteTitle.contains(query) || noteBody.contains(query) &&
                (!isNoteArchived && !isNoteLocked)
    }
}
