package com.ashunevich.simplenotes

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Query("SELECT * FROM note_items")
    fun getAll(): LiveData<List<NoteItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noteItem: NoteItem)

    @Delete
    fun delete(noteItem: NoteItem)

    @Update
    fun update(noteItem: NoteItem)
}