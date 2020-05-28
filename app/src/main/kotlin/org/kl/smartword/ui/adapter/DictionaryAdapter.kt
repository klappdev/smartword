package org.kl.smartword.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.bean.Lesson
import org.kl.smartword.R
import org.kl.smartword.event.lesson.DeleteLessonListener
import org.kl.smartword.ui.EditLessonActivity
import org.kl.smartword.ui.holder.DictionaryViewHolder

class DictionaryAdapter : BaseAdapter {
    internal var listLessons: List<Lesson>
    private var context: Context

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
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

        holder.nameTextView?.text = lesson.name
        holder.dateTextView?.text = lesson.date

        holder.editImageView?.tag = lesson.id
        holder.editImageView?.setOnClickListener(::clickEditLesson)
        holder.deleteImageView?.setOnClickListener(DeleteLessonListener(this, lesson))

        if (lesson.selected) {
            holder.itemImageView?.setImageResource(R.drawable.lesson_selected_icon)

            holder.editImageView?.visibility = View.VISIBLE
            holder.deleteImageView?.visibility = View.VISIBLE
        } else {
            holder.itemImageView?.setImageResource(R.drawable.lesson_icon)

            holder.editImageView?.visibility = View.INVISIBLE
            holder.deleteImageView?.visibility = View.INVISIBLE
        }

        return view
    }

    override fun getItem(position: Int) = listLessons[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = listLessons.size

    private fun clickEditLesson(view: View?) {
        val intent = Intent(context, EditLessonActivity::class.java)
        intent.putExtra("id_lesson", view?.tag as Int)

        context.startActivity(intent)
    }
}