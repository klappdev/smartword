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

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.view.fragment.DictionaryFragment

class SortLessonListener(dictionaryFragment: DictionaryFragment) {
    private val lessonDao = dictionaryFragment.lessonDao
    private val disposables = dictionaryFragment.disposables
    private val dictionaryAdapter = dictionaryFragment.dictionaryAdapter

    private var isAsc: Boolean = false

    operator fun invoke(): Boolean {
        disposables.add(lessonDao.sortByName(isAsc)
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