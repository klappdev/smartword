package org.kl.smartword.event.word

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView

import org.kl.smartword.util.toast
import org.kl.smartword.view.AddWordActivity
import org.kl.smartword.view.EditWordActivity
import org.kl.smartword.view.adapter.WordsAdapter

class NavigateWordListener {
    private val wordsAdapter: WordsAdapter
    private val context: Context
    private val idLesson: Long

    constructor(wordsAdapter: WordsAdapter, idLesson: Long) {
        this.wordsAdapter = wordsAdapter
        this.context = wordsAdapter.context
        this.idLesson = idLesson
    }

    fun navigateAddWord(view: View?) {
        val intent = Intent(context, AddWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_lesson", idLesson)

        context.startActivity(intent)
    }

    fun navigateEditWord(): Boolean {
        val idWord: Long = wordsAdapter.getCurrentItemId()

        val intent = Intent(context, EditWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_word", idWord)

        context.startActivity(intent)
        return true
    }

    fun navigateShowWord(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        context.toast("Show word content")
    }
}