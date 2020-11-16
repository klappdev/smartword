package org.kl.smartword.event.word

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDao
import org.kl.smartword.view.adapter.WordsAdapter
import org.kl.smartword.util.toast

class DeleteWordListener(private val adapter: WordsAdapter,
                         private val word: Word) : View.OnClickListener {
    private lateinit var context: Context

    override fun onClick(view: View?) {
        view?.context ?: return
        this.context = view.context

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete word")
              .setMessage("Do you want delete word?")
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
        val firstDisposable = WordDao.delete(word.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    context.toast("Can't delete word: ${word.name}")
                }
                override fun onComplete() {
                    context.toast("Delete word: ${word.name}")
                }
            })
        firstDisposable.dispose()

        val secondDisposable = WordDao.getAllByIdLesson(word.idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    adapter.listWords = result
                    adapter.notifyDataSetChanged()
                }
            })
        secondDisposable.dispose()
    }
}