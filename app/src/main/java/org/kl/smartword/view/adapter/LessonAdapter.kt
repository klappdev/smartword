package org.kl.smartword.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.R
import org.kl.smartword.model.Word
import org.kl.smartword.view.holder.WordViewHolder

class LessonAdapter : BaseAdapter {
    var context: Context
    var listWords: MutableList<Word>
    var position: Int

    constructor(context: Context, list: MutableList<Word>) {
        this.context = context
        this.listWords = list
        this.position = -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val word = getItem(position)
        val holder: WordViewHolder

        if (view == null) {
            view = LayoutInflater.from(context)
                                 .inflate(R.layout.word_item, parent, false)
            holder = WordViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as WordViewHolder
        }

        holder.bind(word, getCurrentItemId())

        return view
    }

    override fun getCount() = listWords.size
    override fun getItem(position: Int) = listWords[position]
    override fun getItemId(position: Int) = listWords[position].id

    fun getCurrentItem() = getItem(position)
    fun getCurrentItemId() = if (position != -1 && position < count) getItemId(position) else -1
}