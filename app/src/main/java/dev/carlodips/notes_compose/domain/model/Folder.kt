package dev.carlodips.notes_compose.domain.model

import java.time.LocalDateTime

data class Folder (
    val folderId: Int? = null,

    val folderName: String,

    val dateAdded: LocalDateTime = LocalDateTime.now(),

    val dateUpdated: LocalDateTime,
)
