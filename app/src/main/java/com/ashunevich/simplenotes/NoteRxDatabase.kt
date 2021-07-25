package com.ashunevich.simplenotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class],version = 1,exportSchema = false)
abstract class NoteRxDatabase : RoomDatabase() {

    abstract fun notesDao():NoteRxDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRxDatabase? = null

        fun getDatabase(
                context: Context
        ): NoteRxDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRxDatabase::class.java,
                    "note_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}
