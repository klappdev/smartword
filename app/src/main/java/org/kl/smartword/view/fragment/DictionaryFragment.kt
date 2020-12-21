package org.kl.smartword.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.lesson.ChooseLessonListener
import org.kl.smartword.view.MainActivity
import org.kl.smartword.view.adapter.DictionaryAdapter

class DictionaryFragment : Fragment() {
    private lateinit var emptyTextView: TextView
    private lateinit var dictionaryListView: ListView

    private val disposables = CompositeDisposable()
    lateinit var dictionaryAdapter: DictionaryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_dictionary, container, false)
        setHasOptionsMenu(true) 

        this.emptyTextView = rootView.findViewById(R.id.dict_empty_text_view)
        this.dictionaryListView = rootView.findViewById(R.id.dict_list_view)

        dictionaryAdapter = DictionaryAdapter(rootView.context, mutableListOf())

        val mainActivity = (activity as MainActivity)
        mainActivity.initListeners(this)

        with(dictionaryListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            onItemLongClickListener = ChooseLessonListener(mainActivity::notifyMenuItemSelected)
            onItemClickListener = AdapterView.OnItemClickListener(mainActivity.navigateLessonListener::navigateShowWords)
            adapter = dictionaryAdapter
        }

        initLessons()

        return rootView
    }

    override fun onResume() {
        super.onResume()

        initLessons()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initLessons() {
        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    dictionaryAdapter.position = -1
                    dictionaryAdapter.listLessons.clear()
                    dictionaryAdapter.listLessons.addAll(result)
                    dictionaryAdapter.notifyDataSetChanged()
                }
            }))
    }
}