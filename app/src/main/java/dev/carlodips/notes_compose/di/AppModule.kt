package dev.carlodips.notes_compose.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.carlodips.notes_compose.data.local.NotesAppDatabase
import dev.carlodips.notes_compose.data.local.repository.FolderRepositoryImpl
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.data.local.repository.NoteRepositoryImpl
import dev.carlodips.notes_compose.domain.repository.FolderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(app: Application): NotesAppDatabase {
        return Room.databaseBuilder(
            app,
            NotesAppDatabase::class.java,
            NotesAppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NotesAppDatabase): NoteRepository {
        return NoteRepositoryImpl(database.notesDao)
    }

    @Provides
    @Singleton
    fun provideFolderRepository(database: NotesAppDatabase): FolderRepository {
        return FolderRepositoryImpl(database.folderDao)
    }
}
