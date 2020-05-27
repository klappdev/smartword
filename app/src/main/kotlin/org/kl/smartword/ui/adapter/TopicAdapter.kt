package org.kl.smartword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R

import org.kl.smartword.bean.Lesson

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private var context: Context
    internal var listLessons: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.topic_item, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.topicImage?.setImageResource(listLessons[position].icon)
        holder.nameTextView?.text = listLessons[position].name
    }

    override fun getItemCount() = listLessons.size
}
