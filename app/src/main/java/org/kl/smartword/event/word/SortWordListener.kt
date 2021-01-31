package org.kl.smartword.event.word

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word
import org.kl.smartword.view.adapter.LessonAdapter

class SortWordListener {
    private val lessonAdapter: LessonAdapter
    private val disposables: CompositeDisposable
    private var isAsc: Boolean

    constructor(lessonAdapter: LessonAdapter, disposables: CompositeDisposable) {
        this.lessonAdapter = lessonAdapter
        this.disposables = disposables
        this.isAsc = false
    }

    operator fun invoke(): Boolean {
        disposables.add(WordDao.sortByName(isAsc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() { }
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    lessonAdapter.listWords.clear()
                    lessonAdapter.listWords.addAll(result)
                    lessonAdapter.notifyDataSetChanged()

                    isAsc = !isAsc
                }
            }))

        return true
    }
}