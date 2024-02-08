package dev.carlodips.notes_compose.domain.repository

import dev.carlodips.notes_compose.domain.model.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun insertFolder(folder: Folder): Long

    suspend fun deleteFolder(folder: Folder)

    fun getFolders(): Flow<List<Folder>>
}