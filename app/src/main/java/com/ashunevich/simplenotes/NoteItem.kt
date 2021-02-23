package com.ashunevich.simplenotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//DAO
@Entity(tableName = "note_items")
class NoteItem(@PrimaryKey (autoGenerate = false) @ColumnInfo val noteID: Int,
               @ColumnInfo(name = "note_date") val noteDate:String,
               @ColumnInfo(name = "note_text") val noteText: String?,
               @ColumnInfo(name = "note_tag") val noteTag: String?
) {

}