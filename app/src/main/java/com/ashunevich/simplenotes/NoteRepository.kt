package com.ashunevich.simplenotes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<NoteItem>> = noteDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(noteItem: NoteItem) {
        noteDao.insert(noteItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(noteItem: NoteItem){
        noteDao.delete(noteItem)
    }





}