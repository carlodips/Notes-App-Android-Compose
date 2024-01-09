package dev.carlodips.notes_compose.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    val noteId: Int,

    @ColumnInfo(name = "noteTitle")
    val noteTitle: String,

    @ColumnInfo(name = "noteBody")
    val noteBody: String
)