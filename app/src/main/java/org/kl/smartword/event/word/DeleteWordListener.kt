/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2021 https://github.com/klappdev
 *
 * Permission is hereby  granted, free of charge, to any  person obtaining a copy
 * of this software and associated  documentation files (the "Software"), to deal
 * in the Software  without restriction, including without  limitation the rights
 * to  use, copy,  modify, merge,  publish, distribute,  sublicense, and/or  sell
 * copies  of  the Software,  and  to  permit persons  to  whom  the Software  is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE  IS PROVIDED "AS  IS", WITHOUT WARRANTY  OF ANY KIND,  EXPRESS OR
 * IMPLIED,  INCLUDING BUT  NOT  LIMITED TO  THE  WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR  A PARTICULAR PURPOSE AND  NONINFRINGEMENT. IN NO EVENT  SHALL THE
 * AUTHORS  OR COPYRIGHT  HOLDERS  BE  LIABLE FOR  ANY  CLAIM,  DAMAGES OR  OTHER
 * LIABILITY, WHETHER IN AN ACTION OF  CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE  OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kl.smartword.event.word

import android.app.AlertDialog
import android.content.DialogInterface

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.util.toast
import org.kl.smartword.view.activity.ShowLessonActivity

class DeleteWordListener(activity: ShowLessonActivity) {
    private val wordDao = activity.wordDao
    private val disposables = activity.disposables
    private val lessonAdapter = activity.lessonAdapter
    private val context = lessonAdapter.context

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

        disposables.add(wordDao.delete(word.id)
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
        disposables.add(wordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: List<Word> ->
                lessonAdapter.position = -1
                lessonAdapter.listWords.clear()
                lessonAdapter.listWords.addAll(result)
                lessonAdapter.notifyDataSetChanged()
            })
    }
}