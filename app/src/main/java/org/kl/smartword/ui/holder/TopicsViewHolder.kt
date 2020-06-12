package org.kl.smartword.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R

class TopicsViewHolder : RecyclerView.ViewHolder {
    private var topicCardView: CardView? = null
    var topicImage: ImageView? = null
        private set
    var nameTextView: TextView? = null
        private set

    constructor(view: View) : super(view) {
        this.topicCardView = view.findViewById(R.id.topic_card_view)
        this.topicImage = view.findViewById(R.id.item_topic_image)
        this.nameTextView = view.findViewById(R.id.name_text_view)
    }
}