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
import timber.log.Timber

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import org.kl.smartword.ui.word.AddWordActivity
import org.kl.smartword.model.Word
import org.kl.smartword.util.toast

class LoadWordListener(private val activity: AddWordActivity) : View.OnClickListener {
    private val networkConnectivity = activity.networkConnectivity
    private val dictionaryService = activity.dictionaryService
    private val viewValidator = activity.viewValidator
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (viewValidator.validate(activity.nameTextView, "Name is empty")) {
            val name = activity.nameTextView.text.toString().trim()

            checkConnection(name)
        }
    }

    private fun checkConnection(name: String) {
        disposables.add(networkConnectivity.isNetworkAvailable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectionStatus ->
                if (connectionStatus) {
                    loadWordContent(name)
                } else {
                    activity.toast("Network is not available")
                }
            })
    }

    private fun loadWordContent(name: String) {
        val parameters = hashMapOf(
            "action" to "query",
            "prop" to "extracts",
            "redirects" to "",
            "format" to "json",
            "continue" to "",
            "titles" to name
        )

        disposables.add(dictionaryService.getWordContent(parameters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Word>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    activity.toast("Network error: ${e.message}")
                }
                override fun onNext(result: Word) {
                    Timber.d("dictionary page: $result")

                    activity.nameTextView.text = result.name
                    activity.transcriptionTextView.text = result.transcription
                    activity.translationTextView.text = result.translation
                    activity.associationTextView.text = result.association
                    activity.etymologyTextView.text = result.etymology
                    activity.descriptionTextView.text = result.description
                }
            }))
    }
}