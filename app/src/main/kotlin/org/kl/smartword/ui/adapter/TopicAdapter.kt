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
import org.kl.smartword.ui.holder.TopicViewHolder

class TopicAdapter : RecyclerView.Adapter<TopicViewHolder> {
    private var context: Context
    internal var listLessons: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
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
