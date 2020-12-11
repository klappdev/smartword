package org.kl.smartword.event.lesson

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import org.kl.smartword.view.AddLessonActivity
import org.kl.smartword.view.EditLessonActivity
import org.kl.smartword.view.WordsActivity
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.fragment.DictionaryFragment

class NavigateLessonListener {
    private val dictionaryAdapter: DictionaryAdapter
    private val context: Context

    constructor(dictionaryFragment: DictionaryFragment) {
        this.dictionaryAdapter = dictionaryFragment.dictionaryAdapter
        this.context = dictionaryAdapter.context
    }

    fun navigateAddLesson(view: View?) {
        val intent = Intent(context, AddLessonActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateEditLesson(): Boolean {
        val idLesson: Long = dictionaryAdapter.getCurrentItemId()

        val intent = Intent(context, EditLessonActivity::class.java)
        intent.putExtra("id_lesson", idLesson)

        context.startActivity(intent)
        return true
    }

    fun navigateShowWords(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(context, WordsActivity::class.java)
        intent.putExtra("id_lesson", dictionaryAdapter.getItemId(position))

        context.startActivity(intent)
    }
}