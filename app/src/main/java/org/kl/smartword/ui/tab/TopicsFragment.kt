package org.kl.smartword.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.adapter.TopicsAdapter

class TopicsFragment : Fragment() {
    private lateinit var emptyTextView: TextView
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicsAdapter: TopicsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_topic, container, false)
        setHasOptionsMenu(true)

        this.emptyTextView = rootView.findViewById(R.id.topic_empty_text_view)
        this.topicRecyclerView = rootView.findViewById(R.id.topic_recycle_view)

        val layoutManager = GridLayoutManager(context, 2)
        topicRecyclerView.layoutManager = layoutManager

        val listLessons = LessonDB.getAll()

        switchVisibility(listLessons.isNotEmpty())

        this.topicsAdapter = TopicsAdapter(rootView.context, listLessons)
        this.topicRecyclerView.adapter = topicsAdapter

        return rootView
    }

    private fun switchVisibility(flag: Boolean) {
        if (flag) {
            topicRecyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE

        } else {
            topicRecyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        val listLessons = LessonDB.getAll()

        switchVisibility(listLessons.isNotEmpty())

        topicsAdapter.listLessons = listLessons
        topicsAdapter.notifyDataSetChanged()

        Toast.makeText(activity, "Resume topic fragment", Toast.LENGTH_LONG)
             .show()
    }
}