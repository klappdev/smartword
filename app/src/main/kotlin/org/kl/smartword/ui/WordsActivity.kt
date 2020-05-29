package org.kl.smartword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

import org.kl.smartword.R
import org.kl.smartword.bean.Word
import org.kl.smartword.db.WordDB
import org.kl.smartword.ui.adapter.WordsAdapter
import org.kl.smartword.util.formatted
import java.util.*

class WordsActivity : AppCompatActivity() {
    private lateinit var emptyTextView: TextView
    private lateinit var wordsListView: ListView
    private lateinit var wordsAdapter: WordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        this.emptyTextView = findViewById(R.id.word_empty_text_view)
        this.wordsListView = findViewById(R.id.word_list_view)

        with(wordsListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            /*onItemLongClickListener = ManageWordListener()*/
            /*onItemClickListener = AdapterView.OnItemClickListener(::clickShowWord)*/
        }

        val wordDB = WordDB.getInstance(applicationContext)
        wordsAdapter = WordsAdapter(applicationContext, wordDB.getAll())
        wordsListView.adapter = wordsAdapter
    }
}
