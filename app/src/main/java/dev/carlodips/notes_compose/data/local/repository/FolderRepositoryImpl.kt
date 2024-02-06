package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.dao.FolderDao
import dev.carlodips.notes_compose.data.local.entity.FolderEntity
import dev.carlodips.notes_compose.domain.repository.FolderRepository

class FolderRepositoryImpl(
    private val dao: FolderDao
): FolderRepository {
    override suspend fun insertFolder(folder: FolderEntity): Long {
        return dao.insertFolder(folder)
    }

    override suspend fun deleteFolder(folder: FolderEntity) {
        dao.deleteFolder(folder)
    }
}
