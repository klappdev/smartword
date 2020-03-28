package org.kl.smartword.ui.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.adapter.TopicAdapter

private const val ARG_SECTION_NUMBER: String = "main_number"

class TopicFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_topic, container, false)
        setHasOptionsMenu(true)

        val topicRecyclerView = rootView.findViewById<RecyclerView>(R.id.topic_recycle_view)

        val layoutManager = GridLayoutManager(context, 2)
        topicRecyclerView.layoutManager = layoutManager

        val lessonDB = LessonDB.getInstance(fragmentContext)

        val topicAdapter = TopicAdapter(rootView.context, lessonDB.getAll())
        topicRecyclerView.adapter = topicAdapter

        return rootView
    }
}