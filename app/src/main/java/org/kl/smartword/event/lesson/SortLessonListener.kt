package org.kl.smartword.event.lesson

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.view.MainActivity
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.fragment.DictionaryFragment

class SortLessonListener {
    private val activity: MainActivity
    private val disposables: CompositeDisposable
    private val dictionaryAdapter: DictionaryAdapter
    private var isAsc: Boolean = false

    constructor(dictionaryFragment: DictionaryFragment, disposables: CompositeDisposable) {
        this.activity = dictionaryFragment.activity as MainActivity
        this.dictionaryAdapter = dictionaryFragment.dictionaryAdapter
        this.disposables = disposables
    }

    operator fun invoke(): Boolean {
        disposables.add(LessonDao.sortByName(isAsc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() { }
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    dictionaryAdapter.listLessons.clear()
                    dictionaryAdapter.listLessons.addAll(result)
                    dictionaryAdapter.notifyDataSetChanged()

                    isAsc = !isAsc
                }
            }))

        return true
    }
}