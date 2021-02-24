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
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var model: NoteViewModel

    private var binding: ActivityMainBinding? = null
    private val listContentArr: List<NoteEntity> = ArrayList()
    private var adapter: RecyclerViewAdapter? = null



    companion object{
        const val createNoteRequestCode = 1
        const val updateNoteRequestCode = 2
        const val activityCode = "activityCode"
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
            intent.putExtra(activityCode, updateNoteRequestCode)
            startActivityForResult(intent, createNoteRequestCode) }
        attachItemTouchHelper()

        adapter!!.onBind =
            object : UpdateCallbackInterface {
                override fun onViewBound(viewHolder: RecyclerView.ViewHolder?, position: Int) {
                    viewHolder?.itemView?.setOnClickListener {
                        val noteEntity:NoteEntity? = adapter!!.getAccountAtPosition(position)

                        val intent =
                            Intent(this@MainActivity, NoteActivity::class.java)
                            intent.putExtra("itemText", noteEntity?.noteText.toString())
                            intent.putExtra("itemTag", noteEntity?.noteTag.toString())
                            intent.putExtra("itemDate", noteEntity?.noteDate.toString())
                            intent.putExtra("itemID", noteEntity?.noteID)
                            intent.putExtra(activityCode, updateNoteRequestCode)
                        startActivityForResult(intent, updateNoteRequestCode)
                    }
                }
            }
    }

    private fun setRecyclerView(){
        model = ViewModelProvider(this).get(NoteViewModel::class.java)
        adapter = RecyclerViewAdapter(listContentArr)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView?.adapter = adapter
        model.allNotes.observe(this, { notes -> notes?.let { adapter!!.setNOtes(it) } })

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
                val position = viewHolder.adapterPosition
                val noteEntity: NoteEntity? = adapter!!.getAccountAtPosition(position)
                if (noteEntity != null) {
                    model.delete(noteEntity)
                }
            }
        })
        helper.attachToRecyclerView(binding?.recyclerView)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == createNoteRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val note =
                    NoteEntity(
                        getDate(),
                        data.getStringExtra(NoteActivity.MAIN_TEXT),
                        data.getStringExtra(NoteActivity.TAG_TEXT)
                    )
                model.insert(note)
            }
        }

        else if (requestCode == updateNoteRequestCode && resultCode == Activity.RESULT_OK){
            intentData?.let { data ->
                val note = NoteEntity(
                    data.getStringExtra(NoteActivity.DATE_TEXT).toString(),
                    data.getStringExtra(NoteActivity.MAIN_TEXT),
                    data.getStringExtra(NoteActivity.TAG_TEXT),
                    data.getIntExtra(NoteActivity.ID_TXT, 0),
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
