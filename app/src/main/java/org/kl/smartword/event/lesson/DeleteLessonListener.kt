package org.kl.smartword.event.lesson

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.util.toast

class DeleteLessonListener(private val adapter: DictionaryAdapter,
                           private val lesson: Lesson) : View.OnClickListener {
    private lateinit var context: Context

    override fun onClick(view: View?) {
        view?.context ?: return
        this.context = view.context

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete lesson")
              .setMessage("Do you want delete lesson?")
              .setCancelable(false)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setPositiveButton("Yes", ::clickPositiveButton)
              .setNegativeButton("No",  ::clickNegativeButton)
        dialog.show()
    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        /*FIXME: Improve in next revision*/
        val firstDisposable = LessonDao.delete(lesson.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    context.toast("Can't delete lesson: ${lesson.name}")
                }
                override fun onComplete() {
                    context.toast("Delete lesson: ${lesson.name}")
                }
            })
        firstDisposable.dispose()

        val secondDisposable = LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    adapter.listLessons = result
                    adapter.notifyDataSetChanged()
                }
            })
        secondDisposable.dispose()
    }
}