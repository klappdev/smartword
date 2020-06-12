package org.kl.smartword.ui.tab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

import org.kl.smartword.R
import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.lesson.ManageLessonListener
import org.kl.smartword.ui.WordsActivity
import org.kl.smartword.ui.adapter.DictionaryAdapter
import org.kl.smartword.util.formatted

class DictionaryFragment : Fragment() {
    private lateinit var emptyTextView: TextView
    private lateinit var dictionaryListView: ListView
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context

        if (LessonDB.getAll().size < 5) {
            LessonDB.addAll(
                Lesson(1, R.drawable.chemestry_icon, "Chemestry", "", Date().formatted(), false),
                Lesson(2, R.drawable.geography_icon, "Geography", "", Date().formatted(), false),
                Lesson(3, R.drawable.history_icon, "History", "", Date().formatted(), false),
                Lesson(4, R.drawable.math_icon, "Math", "", Date().formatted(), false),
                Lesson(5, R.drawable.physic_icon, "Physic", "", Date().formatted(), false)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_dictionary, container, false)
        setHasOptionsMenu(true) 

        this.emptyTextView = rootView.findViewById(R.id.dict_empty_text_view)
        this.dictionaryListView = rootView.findViewById(R.id.dict_list_view)

        with(dictionaryListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            onItemLongClickListener = ManageLessonListener()
            onItemClickListener = AdapterView.OnItemClickListener(::clickShowWords)
        }

        dictionaryAdapter = DictionaryAdapter(rootView.context, LessonDB.getAll())
        dictionaryListView.adapter = dictionaryAdapter

        return rootView
    }

    override fun onResume() {
        super.onResume()

        dictionaryAdapter.listLessons = LessonDB.getAll()
        dictionaryAdapter.notifyDataSetChanged()
    }

    private fun clickShowWords(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(activity, WordsActivity::class.java)
        intent.putExtra("id_lesson", dictionaryAdapter.getItemId(position).toInt())

        this.startActivity(intent)

        Toast.makeText(parent?.context, "Show words selected lesson ${dictionaryAdapter.getItemId(position).toInt()}", Toast.LENGTH_LONG)
             .show()
    }
}