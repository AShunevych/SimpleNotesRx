package com.ashunevich.simplenotes

import android.app.Application
import androidx.lifecycle.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteRxViewModel (private val dataSource:NoteRxDao) : ViewModel(){

    fun getAll(): Observable<List<NoteEntity>> {
        return dataSource.getAll()
    }

    fun insertEntity(noteEntity: NoteEntity):Completable{
        return dataSource.insert(noteEntity)
    }

    fun delete(noteEntity: NoteEntity) :Completable{
        return dataSource.delete(noteEntity)
    }

    fun update(noteEntity: NoteEntity) :Completable{
        return dataSource.update(noteEntity)
        }
    }



