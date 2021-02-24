package com.ashunevich.simplenotes

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {

    @Query("SELECT * FROM note_items")
    fun getAll(): LiveData<List<NoteEntity>>

    @Insert
    fun insert(noteEntity: NoteEntity)

    @Delete
    fun delete(noteEntity: NoteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg noteEntity: NoteEntity)
}