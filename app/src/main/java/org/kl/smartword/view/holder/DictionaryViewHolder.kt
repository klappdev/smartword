package org.kl.smartword.view.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import org.kl.smartword.R
import org.kl.smartword.model.Lesson

class DictionaryViewHolder {
    private var nameTextView: TextView
    private var dateTextView: TextView
    private var itemImageView: ImageView

    constructor(view: View) {
        this.nameTextView = view.findViewById(R.id.name_lesson_text_view)
        this.dateTextView = view.findViewById(R.id.date_lesson_text_view)
        this.itemImageView = view.findViewById(R.id.item_lesson_image)
    }

    fun bind(item: Lesson, currentId: Long) {
        if (item.id == currentId) {
            itemImageView.setImageResource(R.drawable.lesson_selected_icon)
        } else {
            itemImageView.setImageResource(R.drawable.lesson_icon)
        }

        nameTextView.text = item.name
        dateTextView.text = item.date
    }
}