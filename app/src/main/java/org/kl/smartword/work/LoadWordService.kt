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
package org.kl.smartword.work

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.IntentFilter

import com.google.gson.reflect.TypeToken
import timber.log.Timber

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import javax.inject.Inject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLongArray

import org.kl.smartword.MainApplication
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word
import org.kl.smartword.util.readJsonFileLines

class LoadWordService : JobService() {
    @Inject
    public lateinit var wordDao: WordDao
    @Inject
    public lateinit var receiver: MediateReceiver
    @Inject
    public lateinit var disposables: CompositeDisposable
    private var restartServiceOnDestroy = true

    companion object {
        const val JOB_ID = 2
    }

    override fun onCreate() {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate()

        registerReceiver(receiver, IntentFilter(MediateReceiver.LESSON_IDS_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    override fun onStartJob(parameters: JobParameters): Boolean {
        Timber.d("Start load init word job: ${parameters.jobId}")

        disposables.add(wordDao.checkIfEmpty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                override fun onError(e: Throwable) {
                    Timber.e("Can't check if table empty: ${e.message}")
                    finishJob(parameters)
                }
                override fun onSuccess(isEmpty: Boolean) {
                    if (!isEmpty) {
                        finishJob(parameters)
                    } else {
                        receiveIdLessons(isEmpty, parameters)
                    }
                }
            }))
        return true
    }

    override fun onStopJob(parameters: JobParameters): Boolean {
        Timber.d("Stop load init word job: ${parameters.jobId}")
        disposables.dispose()

        return restartServiceOnDestroy
    }

    private fun finishJob(parameters: JobParameters?) {
        restartServiceOnDestroy = false
        jobFinished(parameters, false)
    }

    private fun receiveIdLessons(isEmpty: Boolean, parameters: JobParameters) {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .takeUntil { receiver.lessonIds != null }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val lessonIds = receiver.lessonIds

                if (isEmpty && lessonIds != null) {
                    loadWords(lessonIds, parameters)
                }
            })
    }

    private fun loadWords(lessonIds: AtomicLongArray, parameters: JobParameters) {
        disposables.add(Observable.fromCallable {
                val wordType = object : TypeToken<List<Word>>(){}.type
                applicationContext.assets.readJsonFileLines<Word>("words.json", wordType)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Timber.e(e, "Error preload words from json")
                    finishJob(parameters)
                }
                override fun onNext(result: List<Word>) {
                    val listWords = mutableListOf<Word>()

                    for (word in result) {
                        val newIdLesson = lessonIds[(word.idLesson - 1).toInt()]
                        listWords += word.copy(idLesson = newIdLesson)
                    }

                    storeWords(listWords, parameters)
                }
            }))
    }

    private fun storeWords(words: List<Word>, parameters: JobParameters) {
        disposables.add(wordDao.addAll(words)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    Timber.e("Can't add words: ${e.message}")
                    finishJob(parameters)
                }
                override fun onComplete() {
                    finishJob(parameters)
                }
            }))
    }
}