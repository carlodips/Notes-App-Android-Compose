package dev.carlodips.notes_compose.data.local.dao

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.carlodips.notes_compose.data.local.NotesDatabase
import dev.carlodips.notes_compose.data.local.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class NotesDaoTest {
    @Inject
    @Named("test_db") //Added named since there is also database from main
    lateinit var database: NotesDatabase

    private lateinit var dao: NotesDao

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()*/
        hiltRule.inject()
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
