package com.ashunevich.simplenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.databinding.NotesItemBinding


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var padList: MutableList<NoteEntity> = mutableListOf()
    private lateinit var context:Context
    private lateinit var noteEntity: NoteEntity
    lateinit var onBind: UpdateCallbackInterface


    //This method inflates view present in the RecyclerView
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder(itemBinding)
    }

    //Binding the data using get() method of POJO object
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(padList[position])
        noteEntity = padList[position]
        holder.bindItems(noteEntity)
        onBind.onViewBound(holder,position)

    }


    override fun getItemCount(): Int {
        return padList.size
    }



    fun getAccountAtPosition(position: Int): NoteEntity {
        return padList[position]
    }

    //View holder class, where all view components are defined
     inner class MyViewHolder(itemBinding: NotesItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val binding:NotesItemBinding = itemBinding
        fun bindItems(noteEntity: NoteEntity){
            binding.dateView.text =noteEntity.noteDate
            binding.noteView.text= noteEntity.noteText
            binding.tagView.text= noteEntity.noteTag
        }
    }

    fun swap(notes: MutableList<NoteEntity>) {
        val diffCallback = RecyclerViewDiffUtil(padList,notes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

       this.padList.clear()
        this.padList.addAll(notes)

        diffResult.dispatchUpdatesTo(this)
    }



}