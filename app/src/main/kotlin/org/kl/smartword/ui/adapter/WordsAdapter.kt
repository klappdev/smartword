package org.kl.smartword.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import org.kl.smartword.R
import org.kl.smartword.bean.Word
import org.kl.smartword.event.word.DeleteWordListener
import org.kl.smartword.ui.holder.WordsViewHolder

class WordsAdapter : BaseAdapter {
    internal var listWords: List<Word>
    private var context: Context

    constructor(context: Context, list: List<Word>) {
        this.context = context
        this.listWords = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val word = getItem(position)
        val holder: WordsViewHolder

        if (view == null) {
            view = LayoutInflater.from(context)
                                 .inflate(R.layout.word_item, parent, false)
            holder = WordsViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as WordsViewHolder
        }

        holder.nameTextView?.text = word.name
        holder.dateTextView?.text = word.date

        holder.editImageView?.tag = word.id
        /*holder.editImageView?.setOnClickListener(::clickEditLesson)*/
        holder.deleteImageView?.setOnClickListener(DeleteWordListener(this, word))

        if (word.selected) {
            holder.itemImageView?.setImageResource(R.drawable.word_selected_icon)

            holder.editImageView?.visibility = View.VISIBLE
            holder.deleteImageView?.visibility = View.VISIBLE
        } else {
            holder.itemImageView?.setImageResource(R.drawable.word_icon)

            holder.editImageView?.visibility = View.INVISIBLE
            holder.deleteImageView?.visibility = View.INVISIBLE
        }

        return view
    }

    override fun getItem(position: Int) = listWords[position]
    override fun getItemId(position: Int) = listWords[position].id.toLong()
    override fun getCount() = listWords.size
}