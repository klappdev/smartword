package org.kl.smartword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R

import org.kl.smartword.bean.Lesson
import org.kl.smartword.ui.holder.TopicsViewHolder

class TopicsAdapter : RecyclerView.Adapter<TopicsViewHolder> {
    private var context: Context
    internal var listLessons: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.topic_item, parent, false)
        return TopicsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        holder.topicImage?.setImageResource(listLessons[position].icon)
        holder.nameTextView?.text = listLessons[position].name
    }

    override fun getItemCount() = listLessons.size
}
