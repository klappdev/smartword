package org.kl.smartword.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R

class TopicViewHolder : RecyclerView.ViewHolder {
    private var topicCardView: CardView? = null
    var topicImage: ImageView? = null
        private set
    var nameTextView: TextView? = null
        private set

    constructor(itemView: View) : super(itemView) {
        this.topicCardView = itemView.findViewById(R.id.topic_card_view)
        this.topicImage = itemView.findViewById(R.id.item_topic_image)
        this.nameTextView = itemView.findViewById(R.id.name_text_view)
    }
}