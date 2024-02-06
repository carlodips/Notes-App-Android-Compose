package dev.carlodips.notes_compose.data.local.dao

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.carlodips.notes_compose.data.local.NotesAppDatabase
import dev.carlodips.notes_compose.data.local.entity.NoteEntity
import dev.carlodips.notes_compose.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class NoteDaoTest {
    @Inject
    @Named("test_db") //Added named since there is also database from main
    lateinit var database: NotesAppDatabase

    private lateinit var dao: NoteDao

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()*/
        hiltRule.inject()
        dao = database.notesDao
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun insertNote_returnsTrue() = runTest {
        val item = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world",
            dateAdded = LocalDateTime.now(),
            dateUpdated = LocalDateTime.now()
        )

        dao.insertNote(NoteEntity.toEntity(item))

        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            dao.getNotes().collect {
                assertThat(it).contains(item)
                latch.countDown()

            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteNote1_returnsTrue() = runBlocking {
        val item1 = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world",
            dateAdded = LocalDateTime.now(),
            dateUpdated = LocalDateTime.now()
        )
        val item2 = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world",
            dateAdded = LocalDateTime.now(),
            dateUpdated = LocalDateTime.now()
        )

        dao.insertNote(NoteEntity.toEntity(item1))
        dao.insertNote(NoteEntity.toEntity(item2))

        dao.deleteNote(NoteEntity.toEntity(item1))

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getNotes().collect {
                assertThat(it).doesNotContain(item1)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()

    }

    @Test
    fun updateNote_returnsTrue() = runBlocking {
        val item = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world",
            dateAdded = LocalDateTime.now(),
            dateUpdated = LocalDateTime.now()
        )
        dao.insertNote(NoteEntity.toEntity(item))

        // create updated word
        val updatedNote = Note(
            noteId = 1,
            noteTitle = "new title",
            noteBody = "Hello world",
            dateAdded = item.dateAdded,
            dateUpdated = LocalDateTime.now()
        )

        // update
        dao.insertNote(NoteEntity.toEntity(updatedNote))

        val result = dao.getNoteById(1)

        assertThat(result?.noteTitle).isEqualTo(updatedNote.noteTitle)
    }

    @Test
    fun updateNoteStillHasSameDateAdded_returnsTrue() = runBlocking {
        val item = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world",
            dateAdded = LocalDateTime.now(),
            dateUpdated = LocalDateTime.now()
        )
        dao.insertNote(NoteEntity.toEntity(item))

        // create updated word
        val updatedNote = Note(
            noteId = 1,
            noteTitle = "new title",
            noteBody = "Hello world",
            dateAdded = item.dateAdded,
            dateUpdated = LocalDateTime.now()
        )

        // update
        dao.insertNote(NoteEntity.toEntity(updatedNote))

        val result = dao.getNoteById(1)

        assertThat(result?.dateAdded).isEqualTo(item.dateAdded)
    }
}
