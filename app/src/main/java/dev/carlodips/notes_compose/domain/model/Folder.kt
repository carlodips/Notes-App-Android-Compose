package dev.carlodips.notes_compose.domain.model

import java.time.LocalDateTime

data class Folder(
    val folderId: Int? = null,

    val folderName: String,

    val dateAdded: LocalDateTime = LocalDateTime.now(),

    val dateUpdated: LocalDateTime,
) {

    companion object {
        val ALL_NOTES = Folder(folderId = 0, folderName = "All", dateUpdated = LocalDateTime.now())
        val UNCATEGORIZED = Folder(folderName = "Uncategorized", dateUpdated = LocalDateTime.now())
    }
}
