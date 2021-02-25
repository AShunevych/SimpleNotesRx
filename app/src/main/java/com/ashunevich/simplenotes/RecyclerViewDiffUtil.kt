package com.ashunevich.simplenotes

import androidx.recyclerview.widget.DiffUtil

class RecyclerViewDiffUtil (private val oldList: MutableList<NoteEntity>,
                            private val newList: MutableList<NoteEntity>): DiffUtil.Callback() {



    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size



    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].noteID == newList[newItemPosition].noteID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }



}