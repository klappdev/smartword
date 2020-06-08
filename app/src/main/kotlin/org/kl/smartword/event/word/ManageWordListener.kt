package org.kl.smartword.event.word

import android.view.View
import android.widget.AdapterView
import org.kl.smartword.R
import org.kl.smartword.ui.adapter.WordsAdapter

class ManageWordListener : AdapterView.OnItemLongClickListener {

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        val baseAdapter = parent?.adapter as WordsAdapter

        for (i in 0 until baseAdapter.count) {
            val word = baseAdapter.getItem(i)

            if (i != position) {
                word.icon = R.drawable.word_icon
                word.selected = false
            } else {
                word.icon = R.drawable.word_selected_icon
                word.selected = true
            }
        }

        baseAdapter.notifyDataSetChanged()

        return true
    }
}