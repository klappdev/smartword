package org.kl.smartword.ui.tab

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.lesson.SelectLessonEvent
import org.kl.smartword.ui.adapter.DictionaryAdapter
import org.kl.smartword.util.formatted
import java.util.*

class DictionaryFragment(var hidden: Boolean = false) : Fragment() {
    lateinit var lessonListView: ListView
        private set
    private lateinit var adapter: DictionaryAdapter
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context

        val lessonDB = LessonDB.getInstance(fragmentContext)

        if (lessonDB.getAll().size < 5) {
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

        lessonListView = rootView.findViewById(R.id.lesson_list_view)

        lessonListView.choiceMode = ListView.CHOICE_MODE_SINGLE
        lessonListView.onItemLongClickListener = SelectLessonEvent()

        val lessonDB = LessonDB.getInstance(fragmentContext)
		
        this.adapter = DictionaryAdapter(rootView.context, lessonDB.getAll())
        lessonListView.adapter = adapter

        return rootView
    }

    override fun onResume() {
        super.onResume()

        val lessonDB = LessonDB.getInstance(fragmentContext)

        adapter.listLessons = lessonDB.getAll()
        adapter.notifyDataSetChanged()

        Toast.makeText(activity, "Updated list lessons", Toast.LENGTH_LONG).show()
    }
}