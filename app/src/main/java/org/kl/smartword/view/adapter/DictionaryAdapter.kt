package org.kl.smartword.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.model.Lesson
import org.kl.smartword.R
import org.kl.smartword.view.holder.DictionaryViewHolder

class DictionaryAdapter : BaseAdapter {
    var context: Context
    var listLessons: MutableList<Lesson>
    var position: Int

    constructor(context: Context, list: MutableList<Lesson>) {
        this.context = context
        this.listLessons = list
        this.position = -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val lesson = getItem(position)
        val holder: DictionaryViewHolder

        if (view == null) {
            view = LayoutInflater.from(context)
                                 .inflate(R.layout.lesson_item, parent, false)
            holder = DictionaryViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as DictionaryViewHolder
        }

        holder.bind(lesson, getCurrentItemId())

        return view
    }

    override fun getCount() = listLessons.size
    override fun getItem(position: Int) = listLessons[position]
    override fun getItemId(position: Int) = listLessons[position].id

    fun getCurrentItem() = getItem(position)
    fun getCurrentItemId() = if (position != -1 && position < count) getItemId(position) else -1
}