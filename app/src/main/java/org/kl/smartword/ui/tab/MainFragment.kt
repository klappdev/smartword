package org.kl.smartword.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.event.lesson.SelectLessonEvent
import org.kl.smartword.ui.adapter.DictionaryAdapter
import org.kl.smartword.util.DateUtil

class MainFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var listLessons: List<Lesson>

    companion object {
        private const val ARG_SECTION_NUMBER: String = "main_number"

        fun newInstance(sectionNumber: Int): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()

            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_dictionary, container, false)
        setHasOptionsMenu(true)

        /* TODO: static data */
        initLessons()

        val lessonListView = rootView.findViewById<ListView>(R.id.lesson_list_view)

        lessonListView.choiceMode = ListView.CHOICE_MODE_SINGLE
        lessonListView.onItemLongClickListener = SelectLessonEvent()

        val dictionaryAdapter = DictionaryAdapter(rootView.context, listLessons)
        lessonListView.adapter = dictionaryAdapter

        return rootView
    }

    private fun initLessons() {
        listLessons = arrayListOf(
            Lesson(1, R.drawable.lesson_icon, "fifth lesson",  "", DateUtil.currentDateTime(), false),
            Lesson(2, R.drawable.lesson_icon, "fourth lesson", "", DateUtil.currentDateTime(), false),
            Lesson(3, R.drawable.lesson_icon, "third lesson",  "", DateUtil.currentDateTime(), false),
            Lesson(4, R.drawable.lesson_icon, "second lesson", "", DateUtil.currentDateTime(), false),
            Lesson(5, R.drawable.lesson_icon, "first lesson",  "", DateUtil.currentDateTime(), false)
        )
    }
}