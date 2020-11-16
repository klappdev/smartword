package org.kl.smartword.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.view.adapter.CategoryAdapter

class CategoryFragment : Fragment() {
    private lateinit var emptyTextView: TextView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)
        setHasOptionsMenu(true)

        this.emptyTextView = rootView.findViewById(R.id.category_empty_text_view)
        this.categoryRecyclerView = rootView.findViewById(R.id.category_recycle_view)
        categoryRecyclerView.layoutManager = GridLayoutManager(context, 2)

        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    categoryAdapter = CategoryAdapter(rootView.context, result)
                    categoryRecyclerView.adapter = categoryAdapter

                    switchVisibility(result.isNotEmpty())
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
                    categoryAdapter.listLessons = result
                    categoryAdapter.notifyDataSetChanged()

                    switchVisibility(result.isNotEmpty())
                }
            }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun switchVisibility(flag: Boolean) {
        if (flag) {
            categoryRecyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE

        } else {
            categoryRecyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        }
    }
}