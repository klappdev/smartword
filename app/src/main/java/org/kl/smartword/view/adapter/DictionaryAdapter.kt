package org.kl.smartword.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.model.Lesson
import org.kl.smartword.R
import org.kl.smartword.event.lesson.DeleteLessonListener
import org.kl.smartword.view.EditLessonActivity
import org.kl.smartword.view.holder.DictionaryViewHolder

class DictionaryAdapter : BaseAdapter {
    private var context: Context
    var listLessons: List<Lesson>

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

        with(holder) {
            editImageView?.tag = lesson.id
            editImageView?.setOnClickListener(::clickEditLesson)
            deleteImageView?.setOnClickListener(DeleteLessonListener(this@DictionaryAdapter, lesson))

            bind(lesson)
        }

        return view
    }

    override fun getItem(position: Int) = listLessons[position]
    override fun getItemId(position: Int) = listLessons[position].id
    override fun getCount() = listLessons.size

    private fun clickEditLesson(view: View?) {
        val intent = Intent(context, EditLessonActivity::class.java)
        intent.putExtra("id_lesson", view?.tag as Long)

        context.startActivity(intent)
    }
}