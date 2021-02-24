package com.ashunevich.simplenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.databinding.NotesItemBinding


class RecyclerViewAdapter(val entities: List<NoteEntity>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private  var padList: List<NoteEntity> = ArrayList()
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
            /*
            val noteItem:NoteItem? = getAccountAtPosition(pos)
            val intent = Intent(context, NoteActivity::class.java)
            if (noteItem != null) {
                intent.putExtra("itemId",noteItem.noteID.toString())
                intent.putExtra("itemText",noteItem.noteText.toString())
                intent.putExtra("itemTag",noteItem.noteTag.toString())
                intent.putExtra("itemDate",noteItem.noteDate)
                (context as Activity).startActivityForResult(intent, updateNoteRequestCode)
            }
             */

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

    internal fun setNOtes(notes: List<NoteEntity>) {
        this.padList = notes
        notifyDataSetChanged()
    }


    fun getAccountAtPosition(position: Int): NoteEntity? {
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



}