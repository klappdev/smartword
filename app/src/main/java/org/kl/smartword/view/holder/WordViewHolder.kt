package org.kl.smartword.view.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import org.kl.smartword.R
import org.kl.smartword.model.Word

class WordViewHolder {
    private val nameTextView: TextView
    private val dateTextView: TextView
    private val itemImageView: ImageView

    constructor(view: View) {
        this.nameTextView = view.findViewById(R.id.name_word_text_view)
        this.dateTextView = view.findViewById(R.id.date_word_text_view)
        this.itemImageView = view.findViewById(R.id.item_word_image)
    }

    fun bind(item: Word, currentId: Long) {
        if (item.id == currentId) {
            itemImageView.setImageResource(R.drawable.word_selected_icon)
        } else {
            itemImageView.setImageResource(R.drawable.word_icon)
        }

        nameTextView.text = item.name
        dateTextView.text = item.date
    }
}