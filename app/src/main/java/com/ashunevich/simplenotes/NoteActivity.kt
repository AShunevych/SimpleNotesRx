package com.ashunevich.simplenotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ashunevich.simplenotes.MainActivity.Companion.ACTIVITY_CODE
import com.ashunevich.simplenotes.MainActivity.Companion.ITEM_ID
import com.ashunevich.simplenotes.MainActivity.Companion.ITEM_TAG
import com.ashunevich.simplenotes.MainActivity.Companion.ITEM_TEXT
import com.ashunevich.simplenotes.MainActivity.Companion.UPDATE_NOTE_CODE
import com.ashunevich.simplenotes.databinding.NoteActivityBinding

class NoteActivity:AppCompatActivity() {
    private var binding: NoteActivityBinding? = null
    private  var id:Int = 0
    private  var intentCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        binding?.submitButton?.setOnClickListener { okOkResult() }

            binding!!.noteText.setText(getIntentText(ITEM_TEXT))
            binding!!.tagText.setText(getIntentText(ITEM_TAG))
            id = getIntentInt(ITEM_ID)

        Log.d(TAG,id.toString())
        intentCode = getIntentInt(ACTIVITY_CODE)

    }

    companion object {
        const val MAIN_TEXT = "mainText"
        const val TAG_TEXT = "tagText"
        const val ID_TXT = "idText"
        const val RESULT_ACTIVITY = "resultActivity"

        private val TAG = NoteActivity::class.java.simpleName
    }

    private fun okOkResult(){
        val replyIntent = Intent()
        if (TextUtils.isEmpty(binding?.noteText?.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }
        else{
            replyIntent.putExtra(RESULT_ACTIVITY,intentCode)
            replyIntent.putExtra(MAIN_TEXT,getText(binding?.noteText))
            replyIntent.putExtra(TAG_TEXT,getText(binding?.tagText))
            if(intentCode == UPDATE_NOTE_CODE) {
                replyIntent.putExtra(ID_TXT,id)
            }
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }
    }

    private fun getText (editText: EditText?):String{
        return editText!!.text.toString()
    }

    private fun getIntentText(intentText:String): String? {
        return intent.getStringExtra(intentText)
    }

    private fun getIntentInt(intentText:String): Int {
        return intent.getIntExtra(intentText,0)
    }
}