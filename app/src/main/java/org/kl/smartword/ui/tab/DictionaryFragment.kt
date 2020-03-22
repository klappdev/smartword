package org.kl.smartword.ui.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.lesson.SelectLessonEvent
import org.kl.smartword.ui.adapter.DictionaryAdapter

class DictionaryFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var fragmentContext: Context

    companion object {
        private const val ARG_SECTION_NUMBER: String = "dictionary_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): DictionaryFragment {
            val fragment = DictionaryFragment()
            val args = Bundle()

            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_dictionary, container, false)
        setHasOptionsMenu(true)

        val listLessons = LessonDB.getInstance(fragmentContext).getAll()

        val lessonListView = rootView.findViewById<ListView>(R.id.lesson_list_view)

        lessonListView.choiceMode = ListView.CHOICE_MODE_SINGLE
        lessonListView.onItemLongClickListener = SelectLessonEvent()

        val dictionaryAdapter = DictionaryAdapter(rootView.context, listLessons)
        lessonListView.adapter = dictionaryAdapter

        return rootView
    }
}