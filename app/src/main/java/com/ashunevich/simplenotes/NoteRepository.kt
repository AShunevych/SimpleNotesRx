package com.ashunevich.simplenotes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<NoteEntity>> = noteDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(noteEntity: NoteEntity) {
        noteDao.insert(noteEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(noteEntity: NoteEntity){
        noteDao.delete(noteEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(noteEntity: NoteEntity){
        noteDao.update(noteEntity)
    }



}