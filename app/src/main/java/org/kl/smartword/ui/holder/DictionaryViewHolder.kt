package org.kl.smartword.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.kl.smartword.R

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

    constructor(view: View) {
        this.nameTextView = view.findViewById(R.id.name_lesson_text_view)
        this.dateTextView = view.findViewById(R.id.date_lesson_text_view)

        this.itemImageView = view.findViewById(R.id.item_lesson_image)
        this.editImageView = view.findViewById(R.id.edit_lesson_image)
        this.deleteImageView = view.findViewById(R.id.delete_lesson_image)
    }
}