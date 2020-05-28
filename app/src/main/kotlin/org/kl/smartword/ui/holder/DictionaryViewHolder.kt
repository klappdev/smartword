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

    constructor(itemView: View) {
        this.nameTextView = itemView.findViewById(R.id.name_text_view)
        this.dateTextView = itemView.findViewById(R.id.date_text_view)

        this.itemImageView = itemView.findViewById(R.id.item_lesson_image)
        this.editImageView = itemView.findViewById(R.id.edit_image)
        this.deleteImageView = itemView.findViewById(R.id.delete_image)
    }
}