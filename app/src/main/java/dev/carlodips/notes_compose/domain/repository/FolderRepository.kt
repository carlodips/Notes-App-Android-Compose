package dev.carlodips.notes_compose.domain.repository

import dev.carlodips.notes_compose.data.local.entity.FolderEntity

interface FolderRepository {
    suspend fun insertFolder(folder: FolderEntity): Long

    suspend fun deleteFolder(folder: FolderEntity)
}