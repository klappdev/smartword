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

import android.view.View

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.view.activity.AddLessonActivity
import org.kl.smartword.util.toast

class AddLessonListener(private val activity: AddLessonActivity) : View.OnClickListener {
    private val lessonDao = activity.lessonDao
    private val viewValidator = activity.viewValidator
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (viewValidator.validate(activity.nameTextView, "Name is empty") &&
            viewValidator.validate(activity.descriptionTextView, "Description is empty")) {

            val name = activity.nameTextView.text.toString()
            val description = activity.descriptionTextView.text.toString()

            checkIfExistLesson(name, description)
        }
    }

    private fun checkIfExistLesson(name: String, description: String) {
        disposables.add(lessonDao.checkIfExists(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                    override fun onError(e: Throwable) {}
                    override fun onSuccess(exist: Boolean) {
                        if (exist) {
                            viewValidator.error(activity.nameTextView, "Lesson already exists")
                        } else {
                            addLesson(Lesson(-1, name, description))
                        }
                    }
                }))
    }

    private fun addLesson(lesson: Lesson) {
        disposables.add(lessonDao.add(lesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't add lesson: ${lesson.name}")
                }
                override fun onComplete() {
                    activity.toast("Added lesson: ${lesson.name}")
                }
            }))
    }
}
