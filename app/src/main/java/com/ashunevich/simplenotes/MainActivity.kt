package com.ashunevich.simplenotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var model: NoteViewModel

    private var binding: ActivityMainBinding? = null
    private var adapter: RecyclerViewAdapter? = null



    companion object{
        const val CREATE_NOTE_CODE = 1
        const val UPDATE_NOTE_CODE = 2
        const val ACTIVITY_CODE = "activityCode"

        const val ITEM_TEXT = "itemText"
        const val ITEM_TAG = "itemTag"
        const val ITEM_ID = "itemID"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setRecyclerView()

        binding?.addNewNote?.setOnClickListener {  val intent = Intent(
            this,
            NoteActivity::class.java
        )
            intent.putExtra(ACTIVITY_CODE, CREATE_NOTE_CODE)
            startActivityForResult(intent, CREATE_NOTE_CODE) }
        attachItemTouchHelper()
        setCallBackInterface()

    }

    private fun setCallBackInterface(){
        adapter!!.onBind =
                object : UpdateCallbackInterface {
                    override fun onViewBound(viewHolder: RecyclerView.ViewHolder?, position: Int) {
                        viewHolder?.itemView?.setOnClickListener {
                            val noteEntity:NoteEntity = adapter!!.getAccountAtPosition(position)

                            val intent =
                                    Intent(this@MainActivity, NoteActivity::class.java)
                            intent.putExtra(ITEM_TEXT, noteEntity.noteText.toString())
                            intent.putExtra(ITEM_TAG, noteEntity.noteTag.toString())
                            intent.putExtra(ITEM_ID, noteEntity.noteID)
                            intent.putExtra(ACTIVITY_CODE, UPDATE_NOTE_CODE)
                            startActivityForResult(intent, UPDATE_NOTE_CODE)
                        }
                    }
                }
    }

    private fun setRecyclerView(){
        model = ViewModelProvider(this).get(NoteViewModel::class.java)
        adapter = RecyclerViewAdapter()
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView?.adapter = adapter
        model.allNotes.observe(this, { notes -> notes?.let { adapter!!.swap(it as MutableList<NoteEntity>) } })

    }

    private fun attachItemTouchHelper(){
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val noteEntity: NoteEntity = adapter!!.getAccountAtPosition(viewHolder.adapterPosition)

                    model.delete(noteEntity)

            }
        })
        helper.attachToRecyclerView(binding?.recyclerView)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == CREATE_NOTE_CODE && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val mainText:String? = data.getStringExtra(NoteActivity.MAIN_TEXT)
                val tagText:String? =  data.getStringExtra(NoteActivity.TAG_TEXT)

                val note = NoteEntity(tagText, mainText, getDate())
                model.insert(note)
            }
        }

        else if (requestCode == UPDATE_NOTE_CODE && resultCode == Activity.RESULT_OK){
            intentData?.let { data ->
                val mainText:String? = data.getStringExtra(NoteActivity.MAIN_TEXT)
                val tagText:String? =  data.getStringExtra(NoteActivity.TAG_TEXT)
                val id:Int =  data.getIntExtra(NoteActivity.ID_TXT, 0)

                val note = NoteEntity(tagText, mainText, getDate(), id,
                )
                 model.update(note)
            }
        }
            else{
            Toast.makeText(
                applicationContext,
                "NOT SAVED",
                Toast.LENGTH_LONG
            ).show()
            }
}

    private fun getDate(): String? {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("d MMM, yyyy ", Locale.UK)
        return df.format(c)
    }

}
