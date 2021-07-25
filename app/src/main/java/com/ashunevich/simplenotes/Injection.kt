package com.ashunevich.simplenotes

import android.content.Context

object Injection {

    private fun provideUserDataSource(context: Context): NoteRxDao {
        val database = NoteRxDatabase.getDatabase(context)
        return database.notesDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
}