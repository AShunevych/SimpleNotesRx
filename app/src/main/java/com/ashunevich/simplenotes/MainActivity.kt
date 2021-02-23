package com.ashunevich.simplenotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var model: RoomViewModel
    private val newWordActivityRequestCode = 1
    private var binding: ActivityMainBinding? = null
    private val listContentArr: List<NoteItem> = ArrayList()
    private var adapter: RecyclerViewAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setRecyclerView()


        binding?.addNewNote?.setOnClickListener {  val intent = Intent(this, NoteActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode) }
    }

    private fun setRecyclerView(){
        model = ViewModelProvider(this).get(RoomViewModel::class.java)
        adapter = RecyclerViewAdapter(listContentArr)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView?.adapter = adapter


        model.allNotes.observe(this, { notes -> notes?.let { adapter!!.setNOtes(it) }})

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val note =
                    NoteItem(23 , "12/23/201", data.getStringExtra("mainText"), data.getStringExtra("tagText"))
                model.insert(note)
            }
        }
            else{
            Toast.makeText(
                applicationContext,
               "NOT SAVED",
                Toast.LENGTH_LONG
            ).show()
            }
}}
