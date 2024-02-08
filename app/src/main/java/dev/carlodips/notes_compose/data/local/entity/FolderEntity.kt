package dev.carlodips.notes_compose.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.carlodips.notes_compose.domain.model.Folder
import java.time.LocalDateTime

@Entity(
    tableName = "Folder"
)
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folderId")
    val folderId: Int? = null,

    @ColumnInfo(name = "folderName")
    val folderName: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "dateUpdated")
    val dateUpdated: LocalDateTime
) {
    companion object {
        fun toEntity(folder: Folder) = FolderEntity(
            folderId = folder.folderId,
            folderName = folder.folderName,
            dateAdded = folder.dateAdded,
            dateUpdated = folder.dateUpdated
        )
    }

    fun toDomain() = Folder(
        folderId = folderId,
        folderName = folderName,
        dateAdded = dateAdded,
        dateUpdated = dateUpdated
    )
}
