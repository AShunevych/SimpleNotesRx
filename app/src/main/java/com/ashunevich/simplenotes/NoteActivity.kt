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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.submitButton?.setOnClickListener { okOkResult() }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
        const val EXTRA_REPLY_1 = "com.example.android.wordlistsql.REPLY1"
    }

    private fun okOkResult(){
        val replyIntent = Intent()
        if (TextUtils.isEmpty(binding?.noteText?.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }
        else{
            replyIntent.putExtra("mainText",binding?.noteText?.text)
            replyIntent.putExtra("tagText",binding?.tagText?.text)
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }
    }
}