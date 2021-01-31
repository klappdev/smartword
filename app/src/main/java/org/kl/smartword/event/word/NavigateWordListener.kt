package org.kl.smartword.event.word

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView

import org.kl.smartword.view.AddWordActivity
import org.kl.smartword.view.EditWordActivity
import org.kl.smartword.view.ShowWordActivity
import org.kl.smartword.view.adapter.LessonAdapter

class NavigateWordListener {
    private val lessonAdapter: LessonAdapter
    private val context: Context
    private val idLesson: Long

    constructor(lessonAdapter: LessonAdapter, idLesson: Long) {
        this.lessonAdapter = lessonAdapter
        this.context = lessonAdapter.context
        this.idLesson = idLesson
    }

    fun navigateAddWord(view: View?) {
        val intent = Intent(context, AddWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_lesson", idLesson)

        context.startActivity(intent)
    }

    fun navigateEditWord(): Boolean {
        val idWord: Long = lessonAdapter.getCurrentItemId()

        val intent = Intent(context, EditWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_word", idWord)

        context.startActivity(intent)
        return true
    }

    fun navigateShowWord(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(context, ShowWordActivity::class.java)
        intent.putExtra("id_word", lessonAdapter.getItemId(position))

        context.startActivity(intent)
    }
}