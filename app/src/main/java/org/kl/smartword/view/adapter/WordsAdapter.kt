package org.kl.smartword.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.R
import org.kl.smartword.model.Word
import org.kl.smartword.event.word.DeleteWordListener
import org.kl.smartword.view.EditWordActivity
import org.kl.smartword.view.holder.WordsViewHolder

class WordsAdapter : BaseAdapter {
    private var context: Context
    var listWords: List<Word>

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

        with(holder) {
            editImageView?.tag = word.id
            editImageView?.setOnClickListener(::clickEditLesson)
            deleteImageView?.setOnClickListener(DeleteWordListener(this@WordsAdapter, word))

            bind(word)
        }

        return view
    }

    override fun getItem(position: Int) = listWords[position]
    override fun getItemId(position: Int) = listWords[position].id
    override fun getCount() = listWords.size

    private fun clickEditLesson(view: View?) {
        val intent = Intent(context, EditWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_word", view?.tag as Long)

        context.startActivity(intent)
    }
}