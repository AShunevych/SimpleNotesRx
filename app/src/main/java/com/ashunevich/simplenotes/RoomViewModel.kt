package com.ashunevich.simplenotes

import android.app.Application
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val allNotes: LiveData<List<NoteItem>>
    init {
        val notesDao = NoteDatabase.getDatabase(application,viewModelScope).notesDao()
        repository = NoteRepository(notesDao)
        allNotes= repository.allNotes
    }

    fun insert(noteItem: NoteItem) = viewModelScope.launch(Dispatchers.IO ){
        repository.insert(noteItem)
    }

}

