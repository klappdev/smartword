package org.kl.smartword.event.word

import android.view.View
import android.widget.AdapterView

import org.kl.smartword.view.adapter.LessonAdapter

class ChooseWordListener(
    var notifyAction: ((Boolean) -> Boolean)?
) : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as LessonAdapter
        baseAdapter.position = position
        baseAdapter.notifyDataSetChanged()

        notifyAction?.invoke(true)
        return true
    }
}