package dev.carlodips.notes_compose.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "Folder"
)
data class FolderEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folderId")
    val folderId: Int? = null,

    @ColumnInfo(name = "folderName")
    val folderName: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "dateUpdated")
    val dateUpdated: LocalDateTime
)
