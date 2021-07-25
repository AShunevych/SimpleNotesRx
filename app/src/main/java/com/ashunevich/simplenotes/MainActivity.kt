package com.ashunevich.simplenotes

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.simplenotes.*
import com.ashunevich.simplenotes.NoteActivity.Companion.ID_TXT
import com.ashunevich.simplenotes.NoteActivity.Companion.MAIN_TEXT
import com.ashunevich.simplenotes.NoteActivity.Companion.RESULT_ACTIVITY
import com.ashunevich.simplenotes.NoteActivity.Companion.TAG_TEXT
import com.ashunevich.simplenotes.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var startActivityForResult: ActivityResultLauncher<Intent>

    private var binding: ActivityMainBinding? = null
    private var adapter: RecyclerViewAdapter? = null

    private lateinit var viewModelFactory: ViewModelFactory

    private val rxViewModel: NoteRxViewModel by viewModels { viewModelFactory }

    private val disposable = CompositeDisposable()

    companion object{
        const val CREATE_NOTE_CODE = 1
        const val UPDATE_NOTE_CODE = 2
        const val ACTIVITY_CODE = "activityCode"

        const val ITEM_TEXT = "itemText"
        const val ITEM_TAG = "itemTag"
        const val ITEM_ID = "itemID"

        private val TAG = MainActivity::class.java.simpleName
    }


    override fun onStart() {
        setResultRequest()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        viewModelFactory = Injection.provideViewModelFactory(this)

        setRecyclerView()
        binding?.addNewNote?.setOnClickListener {  val intent = Intent(
            this,
            NoteActivity::class.java
        )
            intent.putExtra(ACTIVITY_CODE, CREATE_NOTE_CODE)
            startActivityForResult.launch(intent) }
        attachItemTouchHelper()
        setCallBackInterface()

    }

    private fun setResultRequest(){
        startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK) {
                val resultCode:Int? = it.data?.getIntExtra(RESULT_ACTIVITY,0)
                val mainText:String? = it.data?.getStringExtra(MAIN_TEXT)
                val tagText:String? = it.data?.getStringExtra(TAG_TEXT)

                if(resultCode == CREATE_NOTE_CODE){
                    val note = NoteEntity(tagText, mainText, getDate())

                    disposable.add(rxViewModel.insertEntity(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ Log.d(TAG, "Insert Successful")},
                            { error -> Log.e(TAG, "Unable to insert note", error) }))
                }
                else{
                    val id:Int? = it.data?.getIntExtra(ID_TXT, 0)
                    val note = NoteEntity(tagText, mainText, getDate(), id)
                    Log.d(TAG, id.toString())

                    disposable.add(rxViewModel.update(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ Log.d(TAG, "Update Successful")},
                            { error -> Log.e(TAG, "Unable to update username", error) }))

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

    }

    private fun setCallBackInterface(){
        adapter!!.onBind =
                object : UpdateCallbackInterface {
                    override fun onViewBound(viewHolder: RecyclerView.ViewHolder?, position: Int) {
                        viewHolder?.itemView?.setOnClickListener {
                            val noteEntity: NoteEntity = adapter!!.getAccountAtPosition(position)

                            val intent =
                                    Intent(this@MainActivity, NoteActivity::class.java)
                            intent.putExtra(ITEM_TEXT, noteEntity.noteText.toString())
                            intent.putExtra(ITEM_TAG, noteEntity.noteTag.toString())
                            intent.putExtra(ITEM_ID, noteEntity.noteID)
                            intent.putExtra(ACTIVITY_CODE, UPDATE_NOTE_CODE)
                            startActivityForResult.launch(intent)
                        }
                    }
                }
    }

    private fun setRecyclerView(){
        adapter = RecyclerViewAdapter()
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerView?.adapter = adapter


        val obserable: Observable<List<NoteEntity>> = rxViewModel.getAll()
        obserable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {adapter!!.swap(it as MutableList<NoteEntity>) }

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
                disposable.add(rxViewModel.delete(noteEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ Log.d(TAG, "Delete Successful")},
                        { error -> Log.e(TAG, "Unable to update username", error) }))

            }
        })
        helper.attachToRecyclerView(binding?.recyclerView)
    }


    private fun getDate(): String? {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("d MMM, yyyy ", Locale.UK)
        return df.format(c)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}
