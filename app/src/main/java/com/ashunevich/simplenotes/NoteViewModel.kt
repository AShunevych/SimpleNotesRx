package com.ashunevich.simplenotes

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val allNotes: LiveData<List<NoteEntity>>
    init {
        val notesDao = NoteDatabase.getDatabase(application).notesDao()
        repository = NoteRepository(notesDao)
        allNotes= repository.allNotes
    }

    fun insert(noteEntity: NoteEntity) = viewModelScope.launch(Dispatchers.IO ){
        repository.insert(noteEntity)
    }

    fun delete(noteEntity: NoteEntity) = viewModelScope.launch(Dispatchers.IO ){
        repository.delete(noteEntity)
    }

    fun update(noteEntity: NoteEntity) = viewModelScope.launch(Dispatchers.IO ){
        repository.update(noteEntity)
    }

}

