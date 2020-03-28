package org.kl.smartword.ui.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.lesson.SelectLessonEvent
import org.kl.smartword.ui.adapter.DictionaryAdapter
import org.kl.smartword.util.formatted
import java.util.*

private const val ARG_SECTION_NUMBER: String = "dictionary_number"

class DictionaryFragment(var hidden: Boolean = false) : Fragment() {
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context

        val lessonDB = LessonDB.getInstance(fragmentContext)

        if (lessonDB.getAll().size < 10) {
            lessonDB.addAll(
                Lesson(1, R.drawable.lesson_background_1, "Drawing", "", Date().formatted(), false),
                Lesson(1, R.drawable.lesson_background_2, "Geometry", "", Date().formatted(), false),
                Lesson(1, R.drawable.lesson_background_3, "Music", "", Date().formatted(), false),
                Lesson(1, R.drawable.lesson_background_4, "Math", "", Date().formatted(), false),
                Lesson(1, R.drawable.lesson_background_5, "Computer", "", Date().formatted(), false)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_dictionary, container, false)
        setHasOptionsMenu(true) 

        val lessonListView = rootView.findViewById<ListView>(R.id.lesson_list_view)

        lessonListView.choiceMode = ListView.CHOICE_MODE_SINGLE
        lessonListView.onItemLongClickListener = SelectLessonEvent()

        val lessonDB = LessonDB.getInstance(fragmentContext)
		
        val dictionaryAdapter = DictionaryAdapter(rootView.context, lessonDB.getAll())
        lessonListView.adapter = dictionaryAdapter

        return rootView
    }
}