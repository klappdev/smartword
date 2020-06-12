package org.kl.smartword.event.lesson

import android.view.View
import android.widget.AdapterView
import org.kl.smartword.R
import org.kl.smartword.ui.adapter.DictionaryAdapter

class ManageLessonListener : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as DictionaryAdapter

        for (i in 0 until baseAdapter.count) {
            val lesson = baseAdapter.getItem(i)

            if (i != position) {
                lesson.icon = R.drawable.lesson_icon
                lesson.selected = false
            } else {
                lesson.icon = R.drawable.lesson_selected_icon
                lesson.selected = true
            }
        }

        baseAdapter.notifyDataSetChanged()

        return true
    }
}