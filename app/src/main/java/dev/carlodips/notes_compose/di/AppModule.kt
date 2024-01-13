package dev.carlodips.notes_compose.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.carlodips.notes_compose.data.local.NotesDatabase
import dev.carlodips.notes_compose.data.local.repository.NoteRepository
import dev.carlodips.notes_compose.data.local.repository.NotesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(app: Application): NotesDatabase {
        return Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            NotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(database: NotesDatabase): NoteRepository {
        return NotesRepositoryImpl(database.dao)
    }
}
