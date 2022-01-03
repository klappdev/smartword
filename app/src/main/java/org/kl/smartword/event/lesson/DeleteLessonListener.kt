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
package org.kl.smartword.event.lesson

import android.app.AlertDialog
import android.content.DialogInterface

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.ui.lesson.DictionaryFragment
import org.kl.smartword.util.toast

class DeleteLessonListener(dictionaryFragment: DictionaryFragment) {
    private val lessonDao = dictionaryFragment.lessonDao
    private val disposables = dictionaryFragment.disposables
    private val dictionaryAdapter = dictionaryFragment.dictionaryAdapter
    private val context = dictionaryAdapter.context

    operator fun invoke(): Boolean {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete lesson")
            .setMessage("Do you want delete lesson?")
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes", ::clickPositiveButton)
            .setNegativeButton("No",  ::clickNegativeButton)
        dialog.show()

        return true
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        val lesson = dictionaryAdapter.getCurrentItem()

        disposables.add(lessonDao.delete(lesson.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen { refreshLessons() }
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    context.toast("Can't delete lesson: ${lesson.name}")
                }
                override fun onComplete() {
                    context.toast("Delete lesson: ${lesson.name}")
                }
            }))

    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }

    private fun refreshLessons() {
        disposables.add(lessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: List<Lesson> ->
                dictionaryAdapter.position = -1
                dictionaryAdapter.listLessons.clear()
                dictionaryAdapter.listLessons.addAll(result)
                dictionaryAdapter.notifyDataSetChanged()
            })
    }
}