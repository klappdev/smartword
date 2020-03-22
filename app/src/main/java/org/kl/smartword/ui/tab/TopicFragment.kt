package org.kl.smartword.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.ui.adapter.TopicAdapter
import org.kl.smartword.util.DateUtil

class TopicFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var listLessons: List<Lesson>

    companion object {
        private const val ARG_SECTION_NUMBER: String = "main_number"

        fun newInstance(sectionNumber: Int): TopicFragment {
            val fragment = TopicFragment()
            val args = Bundle()

            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_topic, container, false)
        setHasOptionsMenu(true)

        /* TODO: static data */
        initLessons()

        val topicRecyclerView = rootView.findViewById<RecyclerView>(R.id.topic_recycle_view)

        val layoutManager = GridLayoutManager(context, 2)
        topicRecyclerView.layoutManager = layoutManager

        val topicAdapter = TopicAdapter(rootView.context, listLessons)
        topicRecyclerView.adapter = topicAdapter

        return rootView
    }

    private fun initLessons() {
        listLessons = arrayListOf(
            Lesson(1, R.drawable.lesson_background,   "Drawing", "", DateUtil.currentDateTime(), true),
            Lesson(2, R.drawable.lesson_background_1, "Geometry", "", DateUtil.currentDateTime(), true),
            Lesson(3, R.drawable.lesson_background_2, "Music", "", DateUtil.currentDateTime(), true),
            Lesson(4, R.drawable.lesson_background_3, "Math", "", DateUtil.currentDateTime(), true),
            Lesson(5, R.drawable.lesson_background_4, "Computer", "", DateUtil.currentDateTime(), true)
        )
    }
}