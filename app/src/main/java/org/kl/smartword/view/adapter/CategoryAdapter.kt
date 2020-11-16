package org.kl.smartword.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import org.kl.smartword.R
import org.kl.smartword.model.Lesson
import org.kl.smartword.view.holder.CategoryViewHolder

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder> {
    private var context: Context
    var listLessons: List<Lesson>

    constructor(context: Context, list: List<Lesson>) {
        this.context = context
        this.listLessons = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val lesson = listLessons[position]
        holder.bind(lesson)
    }

    override fun getItemCount() = listLessons.size
}
