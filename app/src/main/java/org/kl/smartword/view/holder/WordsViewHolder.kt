package org.kl.smartword.view.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import org.kl.smartword.R
import org.kl.smartword.model.Word

class WordsViewHolder {
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

    companion object {
        @JvmStatic
        internal var currentPosition: Long = -1
    }

    constructor(view: View) {
        this.nameTextView = view.findViewById(R.id.name_word_text_view)
        this.dateTextView = view.findViewById(R.id.date_word_text_view)

        this.itemImageView = view.findViewById(R.id.item_word_image)
        this.editImageView = view.findViewById(R.id.edit_word_image)
        this.deleteImageView = view.findViewById(R.id.delete_word_image)
    }

    fun bind(item: Word) {
        if (item.id == currentPosition) {
            itemImageView?.setImageResource(R.drawable.word_selected_icon)

            editImageView?.visibility = View.VISIBLE
            deleteImageView?.visibility = View.VISIBLE
        } else {
            itemImageView?.setImageResource(R.drawable.word_icon)

            editImageView?.visibility = View.INVISIBLE
            deleteImageView?.visibility = View.INVISIBLE
        }

        nameTextView?.text = item.name
        dateTextView?.text = item.date
    }
}