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

class DictionaryAdapter : BaseAdapter {
    private var context: Context
    private var inflater: LayoutInflater
    private var list: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.list = list

        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val lesson = getItem(position)

        if (view == null) {
            view = inflater.inflate(R.layout.lesson_item, parent, false)
        }

        val nameTextView = view?.findViewById<TextView>(R.id.name_text_view)
        val dateTextView = view?.findViewById<TextView>(R.id.date_text_view)

        val itemImageView = view?.findViewById<ImageView>(R.id.item_lesson_image)
        val editImageView = view?.findViewById<ImageView>(R.id.edit_image)
        val deleteImageView = view?.findViewById<ImageView>(R.id.delete_image)

        nameTextView?.text = lesson.name
        dateTextView?.text = lesson.date

        if (lesson.selected) {
            itemImageView?.setImageResource(R.drawable.lesson_selected_icon)

            editImageView?.visibility = View.VISIBLE
            deleteImageView?.visibility = View.VISIBLE
        } else {
            itemImageView?.setImageResource(R.drawable.lesson_icon)

            editImageView?.visibility = View.INVISIBLE
            deleteImageView?.visibility = View.INVISIBLE
        }

        return view
    }

    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = list.size
}