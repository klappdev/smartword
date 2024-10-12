/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2024 https://github.com/klappdev
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

import android.view.View

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.util.toast
import org.kl.smartword.ui.word.EditWordActivity

class EditWordListener(private val activity: EditWordActivity,
                       private val idWord: Long,
                       private val idLesson: Long) : View.OnClickListener {
    private val wordDao = activity.wordDao
    private val viewValidator = activity.viewValidator
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (viewValidator.validate(activity.nameTextView, "Name is empty") &&
            viewValidator.validate(activity.transcriptionTextView, "transcription is empty") &&
            viewValidator.validate(activity.translationTextView, "translation is empty") &&
            viewValidator.validate(activity.associationTextView, "association is empty") &&
            viewValidator.validate(activity.etymologyTextView, "etymology is empty") &&
            viewValidator.validate(activity.descriptionTextView, "description is empty")) {

            val newWord = Word(idWord, idLesson,
                    name = activity.nameTextView.text.toString(),
                    transcription = activity.transcriptionTextView.text.toString(),
                    translation = activity.translationTextView.text.toString(),
                    association = activity.associationTextView.text.toString(),
                    etymology = activity.etymologyTextView.text.toString(),
                    description = activity.descriptionTextView.text.toString())

            updateWord(newWord)
        }
    }

    private fun updateWord(newWord: Word) {
        disposables.add(wordDao.update(newWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't update word: ${newWord.name}")
                }
                override fun onComplete() {
                    activity.toast("Update word: ${newWord.name}")
                }
            }))
    }
}