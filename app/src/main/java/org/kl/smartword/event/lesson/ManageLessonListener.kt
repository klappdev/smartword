package org.kl.smartword.event.lesson

import android.view.View
import android.widget.AdapterView

import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.holder.DictionaryViewHolder

class ManageLessonListener : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as DictionaryAdapter

        DictionaryViewHolder.currentPosition = baseAdapter.getItemId(position)
        baseAdapter.notifyDataSetChanged()

        return true
    }
}