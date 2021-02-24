package com.ashunevich.simplenotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.ashunevich.simplenotes.databinding.ActivityMainBinding
import com.ashunevich.simplenotes.databinding.NoteActivityBinding

class NoteActivity:AppCompatActivity() {
    private var binding: NoteActivityBinding? = null
    private lateinit var date:String
    private  var id:Int = 0
    private  var intentCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.submitButton?.setOnClickListener { okOkResult() }

            binding!!.noteText.setText(intent.getStringExtra("itemText"))
            binding!!.tagText.setText(intent.getStringExtra("itemTag"))
            date = intent.getStringExtra("itemDate").toString()
            id = intent.getIntExtra("itemID",0)
        intentCode = intent.getIntExtra(MainActivity.activityCode,0)

    }

    companion object {
        const val MAIN_TEXT = "mainText"
        const val TAG_TEXT = "tagText"
        const val DATE_TEXT = "dateText"
        const val ID_TXT = "idText"
    }

    private fun okOkResult(){
        val replyIntent = Intent()
        if (TextUtils.isEmpty(binding?.noteText?.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }
        else{
            if(intentCode == 1){
                replyIntent.putExtra(MAIN_TEXT,binding?.noteText?.text.toString())
                replyIntent.putExtra(TAG_TEXT,binding?.tagText?.text.toString())
            }
            else {
                replyIntent.putExtra(MAIN_TEXT,binding?.noteText?.text.toString())
                replyIntent.putExtra(TAG_TEXT,binding?.tagText?.text.toString())
                replyIntent.putExtra(ID_TXT,id)
                replyIntent.putExtra(DATE_TEXT,date)
            }
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }
    }
}