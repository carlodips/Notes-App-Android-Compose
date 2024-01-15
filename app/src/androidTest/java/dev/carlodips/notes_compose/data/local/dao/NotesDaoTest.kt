package dev.carlodips.notes_compose.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dev.carlodips.notes_compose.data.local.NotesDatabase
import dev.carlodips.notes_compose.data.local.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
@SmallTest
class NotesDaoTest {
    private lateinit var database: NotesDatabase
    private lateinit var dao: NotesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao
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
            noteBody = "Hello world"
        )

        dao.insertNote(item)

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
            noteBody = "Hello world"
        )
        val item2 = Note(
            noteId = 1,
            noteTitle = "Testing",
            noteBody = "Hello world"
        )

        dao.insertNote(item1)
        dao.insertNote(item2)

        dao.deleteNote(item1)

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
            noteBody = "Hello world"
        )
        dao.insertNote(item)

        // create updated word
        val updatedNote = Note(
            noteId = 1,
            noteTitle = "new title",
            noteBody = "Hello world"
        )

        // update
        dao.insertNote(updatedNote)


        val result = dao.getNoteById(1)

        assertThat(result?.noteTitle).isEqualTo(updatedNote.noteTitle)
    }
}
