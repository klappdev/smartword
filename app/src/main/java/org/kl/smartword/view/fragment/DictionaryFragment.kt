package org.kl.smartword.view.fragment

import android.content.Context
import android.content.Intent
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
import org.kl.smartword.event.lesson.ManageLessonListener
import org.kl.smartword.view.WordsActivity
import org.kl.smartword.view.adapter.DictionaryAdapter

class DictionaryFragment : Fragment() {
    private lateinit var emptyTextView: TextView
    private lateinit var dictionaryListView: ListView
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private lateinit var fragmentContext: Context
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentContext = context
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

        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    dictionaryAdapter = DictionaryAdapter(rootView.context, result)
                    dictionaryListView.adapter = dictionaryAdapter
                }
            }))

        return rootView
    }

    override fun onResume() {
        super.onResume()

        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    dictionaryAdapter.listLessons = result
                    dictionaryAdapter.notifyDataSetChanged()
                }
            }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun clickShowWords(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(activity, WordsActivity::class.java)
        intent.putExtra("id_lesson", dictionaryAdapter.getItemId(position))

        this.startActivity(intent)
    }
}