package org.kl.smartword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import org.kl.smartword.bean.Lesson
import org.kl.smartword.R
import org.kl.smartword.event.lesson.DeleteLessonEvent

class DictionaryAdapter : BaseAdapter {
    private var context: Context
    internal var listLessons: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
    }

    class DictionaryViewHolder {
        var nameTextView: TextView? = null
            private set
        var dateTextView: TextView? = null
            private set

        var itemImageView: ImageView? = null
            private set
        var editImageView: ImageView? = null
            private set
        var deleteImageView: ImageView? = null
            private set

        constructor(itemView: View) {
            this.nameTextView = itemView.findViewById(R.id.name_text_view)
            this.dateTextView = itemView.findViewById(R.id.date_text_view)

            this.itemImageView = itemView.findViewById(R.id.item_lesson_image)
            this.editImageView = itemView.findViewById(R.id.edit_image)
            this.deleteImageView = itemView.findViewById(R.id.delete_image)
        }
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
        holder.deleteImageView?.setOnClickListener(DeleteLessonEvent(this, lesson))


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
}