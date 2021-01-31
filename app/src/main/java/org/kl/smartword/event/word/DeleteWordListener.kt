package org.kl.smartword.event.word

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDao
import org.kl.smartword.view.adapter.LessonAdapter
import org.kl.smartword.util.toast

class DeleteWordListener {
    private val lessonAdapter: LessonAdapter
    private val disposables: CompositeDisposable
    private val context: Context

    constructor(lessonAdapter: LessonAdapter, disposables: CompositeDisposable) {
        this.lessonAdapter = lessonAdapter
        this.disposables = disposables
        this.context = lessonAdapter.context
    }

    operator fun invoke(): Boolean {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete word")
            .setMessage("Do you want delete word?")
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes", ::clickPositiveButton)
            .setNegativeButton("No",  ::clickNegativeButton)
        dialog.show()

        return true
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        val word = lessonAdapter.getCurrentItem()

        disposables.add(WordDao.delete(word.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen { refreshWords(word.idLesson) }
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    context.toast("Can't delete word: ${word.name}")
                }
                override fun onComplete() {
                    context.toast("Delete word: ${word.name}")
                }
            }))
    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }

    private fun refreshWords(idLesson: Long) {
        disposables.add(WordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    lessonAdapter.position = -1
                    lessonAdapter.listWords.clear()
                    lessonAdapter.listWords.addAll(result)
                    lessonAdapter.notifyDataSetChanged()
                }
            }))
    }
}