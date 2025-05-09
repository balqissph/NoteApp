package com.balqis.noteapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = database.noteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertNote_savesNoteCorrectly() = runBlocking {
        val note = Note(title = "Judul Test", content = "Isi catatan test")
        noteDao.insert(note)

        val result = noteDao.getAll()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Judul Test", result[0].title)
        Assert.assertEquals("Isi catatan test", result[0].content)
    }

    @Test
    fun getAll_returnsAllInsertedNotes() = runBlocking {
        val note1 = Note(title = "Catatan 1", content = "Isi 1")
        val note2 = Note(title = "Catatan 2", content = "Isi 2")
        noteDao.insert(note1)
        noteDao.insert(note2)

        val result = noteDao.getAll()
        Assert.assertEquals(2, result.size)
    }
}
