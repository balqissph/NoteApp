package com.balqis.noteapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.balqis.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel.noteDatabase = NoteDatabase.getInstance(this)
        binding.viewModel = noteViewModel
        binding.lifecycleOwner = this

        noteAdapter = NoteAdapter(emptyList())

        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = noteAdapter

        val editTitle = binding.editTitle
        val editContent = binding.editContent

        binding.buttonSave.setOnClickListener {
            val title = editTitle.text.toString()
            val content = editContent.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                val note = Note(title = title, content = content)
                noteViewModel.insertNote(note)
                editTitle.text.clear()
                editContent.text.clear()
            } else {
                noteViewModel.setErrorMessage("Judul dan isi tidak boleh kosong")
            }
        }

        noteViewModel.notes.observe(this) { notes ->
            noteAdapter.updateNotes(notes)
        }

        noteViewModel.loadNotes()
    }
}
