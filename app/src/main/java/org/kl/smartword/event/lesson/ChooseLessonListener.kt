package org.kl.smartword.event.lesson

import android.view.View
import android.widget.AdapterView

import org.kl.smartword.view.adapter.DictionaryAdapter

class ChooseLessonListener(
    var notifyAction: ((Boolean) -> Boolean)?
) : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as DictionaryAdapter

        baseAdapter.position = position
        baseAdapter.notifyDataSetChanged()

        notifyAction?.invoke(true)

        return true
    }
}