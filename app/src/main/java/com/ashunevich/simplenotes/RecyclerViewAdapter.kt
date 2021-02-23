package com.ashunevich.simplenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.databinding.NotesItemBinding

class RecyclerViewAdapter (val items : List<NoteItem>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    //Creating an arraylist of POJO objects
    private  var padList: List<NoteItem> = ArrayList()


    //This method inflates view present in the RecyclerView
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)

    }

    //Binding the data using get() method of POJO object
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(padList[position])
        val stopwatchPOJO: NoteItem = padList[position]
        holder.bindItems(stopwatchPOJO)
    }


    override fun getItemCount(): Int {
        return padList.size
    }

    internal fun setNOtes(notes: List<NoteItem>) {
        this.padList = notes
        notifyDataSetChanged()
    }



    //View holder class, where all view components are defined
     inner class MyViewHolder(itemBinding: NotesItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val binding:NotesItemBinding = itemBinding
        fun bindItems(noteItem: NoteItem){
            binding.dateView.text =noteItem.noteDate
            binding.noteView.text= noteItem.noteText
            binding.tagView.text= noteItem.noteTag
        }
    }


}