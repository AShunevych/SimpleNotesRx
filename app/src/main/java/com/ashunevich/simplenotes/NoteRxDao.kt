package com.ashunevich.simplenotes

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable


@Dao
interface NoteRxDao {

    @Query("SELECT * FROM note_items ORDER BY noteID DESC")
    fun getAll(): Observable<List<NoteEntity>>

    @Insert
    fun insert(noteEntity: NoteEntity) : Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg noteEntity: NoteEntity) : Completable

}