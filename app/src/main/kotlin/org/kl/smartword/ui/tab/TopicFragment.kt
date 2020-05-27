package org.kl.smartword.ui.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.adapter.TopicAdapter

class TopicFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_topic, container, false)
        setHasOptionsMenu(true)

        this.topicRecyclerView = rootView.findViewById(R.id.topic_recycle_view)

        val layoutManager = GridLayoutManager(context, 2)
        topicRecyclerView.layoutManager = layoutManager

        val lessonDB = LessonDB.getInstance(fragmentContext)

        this.topicAdapter = TopicAdapter(rootView.context, lessonDB.getAll())
        topicRecyclerView.adapter = topicAdapter

        return rootView
    }

    override fun onResume() {
        super.onResume()

        val lessonDB = LessonDB.getInstance(fragmentContext)

        if (lessonDB.countRows() != topicAdapter.listLessons.size) {
            topicAdapter.listLessons = lessonDB.getAll()
            topicAdapter.notifyDataSetChanged()
        }
    }
}