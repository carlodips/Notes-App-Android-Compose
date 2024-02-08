package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.dao.FolderDao
import dev.carlodips.notes_compose.data.local.entity.FolderEntity
import dev.carlodips.notes_compose.domain.model.Folder
import dev.carlodips.notes_compose.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepositoryImpl(
    private val dao: FolderDao
): FolderRepository {
    override suspend fun insertFolder(folder: Folder): Long {
        return dao.insertFolder(FolderEntity.toEntity(folder))
    }

    override suspend fun deleteFolder(folder: Folder) {
        dao.deleteFolder(FolderEntity.toEntity(folder))
    }

    override fun getFolders(): Flow<List<Folder>> {
        return dao.getFolders().map {list ->
            list.map { folderEntity ->
                folderEntity.toDomain()
            }
        }
    }
}
