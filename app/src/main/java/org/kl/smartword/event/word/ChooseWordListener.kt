package org.kl.smartword.event.word

import android.view.View
import android.widget.AdapterView

import org.kl.smartword.view.adapter.WordsAdapter
import org.kl.smartword.view.holder.WordsViewHolder

class ChooseWordListener : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as WordsAdapter

        WordsViewHolder.currentPosition = baseAdapter.getItemId(position)
        baseAdapter.notifyDataSetChanged()

        return true
    }
}