package org.kl.smartword.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

import org.kl.smartword.R
import org.kl.smartword.bean.Word
import org.kl.smartword.db.WordDB
import org.kl.smartword.event.word.ManageWordListener
import org.kl.smartword.ui.adapter.WordsAdapter
import org.kl.smartword.util.formatted
import java.util.*

class WordsActivity : AppCompatActivity() {
    private lateinit var emptyTextView: TextView
    private lateinit var wordsListView: ListView
    private lateinit var wordsAdapter: WordsAdapter
    private lateinit var addWordButton: FloatingActionButton
    private var idLesson: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        this.emptyTextView = findViewById(R.id.word_empty_text_view)
        this.wordsListView = findViewById(R.id.word_list_view)

        with(wordsListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            onItemLongClickListener = ManageWordListener()
            /*onItemClickListener = AdapterView.OnItemClickListener(::clickShowWord)*/
        }

        this.addWordButton = findViewById(R.id.add_word_button)
        this.addWordButton.setOnClickListener(::clickAddWord)

        this.idLesson = intent.getIntExtra("id_lesson", -1)

        wordsAdapter = WordsAdapter(this.baseContext, WordDB.getAllByIdLesson(idLesson))
        wordsListView.adapter = wordsAdapter
    }

    override fun onResume() {
        super.onResume()

        wordsAdapter.listWords = WordDB.getAllByIdLesson(idLesson)
        wordsAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Resume words activity", Toast.LENGTH_LONG)
             .show()
    }

    private fun clickAddWord(view: View?) {
        val intent = Intent(this, AddWordActivity::class.java)
        intent.putExtra("id_lesson", idLesson)

        this.startActivity(intent)
    }
}
