package com.balqis.noteapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    var noteDatabase: NoteDatabase? = null

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> get() = _notes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDatabase?.noteDao()?.insert(note)
            loadNotes()
        }
    }

    fun loadNotes() {
        viewModelScope.launch {
            val notesList = noteDatabase?.noteDao()?.getAll() ?: emptyList()
            _notes.postValue(notesList)
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
