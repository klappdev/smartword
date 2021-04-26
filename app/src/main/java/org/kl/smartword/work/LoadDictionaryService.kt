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
package org.kl.smartword.work

import android.util.Log
import android.app.job.JobParameters
import android.app.job.JobService

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.db.LessonDao
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.model.Word
import org.kl.smartword.MainApplication

class LoadDictionaryService : JobService() {
    @Inject
    public lateinit var wordDao: WordDao

    @Inject
    public  lateinit var lessonDao: LessonDao

    @Inject
    public lateinit var disposables: CompositeDisposable
	private var restartServiceOnDestroy = true

    companion object {
		private const val TAG = "LILS-TAG"
        public  const val JOB_ID = 1
    }

    override fun onCreate() {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate()
    }

	override fun onStartJob(parameters: JobParameters): Boolean {
		Log.i(TAG, "Start load init lesson job: ${parameters.jobId}")

        disposables.add(lessonDao.checkIfEmpty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Can't check if table empty: ${e.message}")
                    finishJob(parameters)
                }
                override fun onSuccess(isEmpty: Boolean) {
                    if (isEmpty) {
                        loadLessons(parameters)
                    } else {
                        finishJob(parameters)
                    }
                }
            }))

		return true
	}

	override fun onStopJob(parameters: JobParameters): Boolean {
		Log.i(TAG, "Stop load init lesson job: ${parameters.jobId}")
        disposables.dispose()

		return restartServiceOnDestroy;
	}

	private fun finishJob(parameters: JobParameters?) {
        restartServiceOnDestroy = false
        jobFinished(parameters, false)
    }

    private fun loadLessons(parameters: JobParameters) {
        disposables.add(Observable.fromCallable(::loadLessonsSynchronously)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Error preload lessons from json", e)
                    finishJob(parameters)
                }
                override fun onNext(result: List<Lesson>) {
                    lessonDao.dataSubject.onNext(result)
                    lessonDao.dataSubject.onComplete()

                    storeLessons(result, parameters)
                }
            }))
    }

    private fun loadWords(listIdLessons: List<Long>, parameters: JobParameters) {
        disposables.add(Observable.fromCallable(::loadWordsSynchronously)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Error preload words from json", e)
                    finishJob(parameters)
                }
                override fun onNext(result: List<Word>) {
                    val listWords = mutableListOf<Word>()

                    for (word in result) {
                        val newIdLesson = listIdLessons[(word.idLesson - 1).toInt()]
                        listWords += word.copy(idLesson = newIdLesson)
                    }

                    storeWords(listWords, parameters)
                }
            }))
    }

    @Throws(Exception::class)
    private fun loadWordsSynchronously(): List<Word> {
        val words = mutableListOf<Word>()

        applicationContext.assets.open("words.json").use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val wordType = object : TypeToken<List<Word>>(){}.type
                words.addAll(Gson().fromJson(jsonReader, wordType))
            }
        }

        return words
    }

    @Throws(Exception::class)
    private fun loadLessonsSynchronously(): List<Lesson> {
        val lessons = mutableListOf<Lesson>()

        applicationContext.assets.open("lessons.json").use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val lessonType = object : TypeToken<List<Lesson>>(){}.type
                lessons.addAll(Gson().fromJson(jsonReader, lessonType))
            }
        }

        return lessons
    }

    private fun getIdLessons(parameters: JobParameters) {
        disposables.add(lessonDao.getAllIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Long>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Can't load id lessons: ${e.message}")
                    finishJob(parameters)
                }
                override fun onNext(result: List<Long>) {
                    loadWords(result, parameters)
                }
            }))
    }

    private fun storeLessons(lessons: List<Lesson>, parameters: JobParameters) {
        disposables.add(lessonDao.addAll(lessons)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Can't add lessons: ${e.message}")
                    finishJob(parameters)
                }
                override fun onComplete() {
                    getIdLessons(parameters)
                }
            }))
    }

    private fun storeWords(words: List<Word>, parameters: JobParameters) {
        disposables.add(wordDao.addAll(words)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    Log.e(TAG, "Can't add words: ${e.message}")
                    finishJob(parameters)
                }
                override fun onComplete() {
                    finishJob(parameters)
                }
            }))
    }
}
