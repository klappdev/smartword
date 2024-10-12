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
import android.content.Intent

import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import timber.log.Timber

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import org.kl.smartword.MainApplication
import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.util.readJsonFileLines

class LoadLessonService : JobService() {
    @Inject
    public lateinit var lessonDao: LessonDao

    @Inject
    public lateinit var disposables: CompositeDisposable
	private var restartServiceOnDestroy = true

    companion object {
        const val JOB_ID = 1
    }

    override fun onCreate() {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate()
    }

	override fun onStartJob(parameters: JobParameters): Boolean {
		Timber.d("Start load init lesson job: ${parameters.jobId}")

        disposables.add(lessonDao.checkIfEmpty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                override fun onError(e: Throwable) {
                    Timber.e("Can't check if table empty: ${e.message}")
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
        Timber.d("Stop load init lesson job: ${parameters.jobId}")
        disposables.dispose()

		return restartServiceOnDestroy
	}

	private fun finishJob(parameters: JobParameters?) {
        restartServiceOnDestroy = false
        jobFinished(parameters, false)
    }

    private fun loadLessons(parameters: JobParameters) {
        disposables.add(Observable.fromCallable {
                val lessonType = object : TypeToken<List<Lesson>>(){}.type
                applicationContext.assets.readJsonFileLines<Lesson>("lessons.json", lessonType)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Timber.e(e, "Error load lessons from json")
                    finishJob(parameters)
                }
                override fun onNext(result: List<Lesson>) {
                    lessonDao.dataSubject.onNext(result)
                    lessonDao.dataSubject.onComplete()

                    storeLessons(result, parameters)
                }
            }))
    }

    private fun storeLessons(lessons: List<Lesson>, parameters: JobParameters) {
        disposables.add(lessonDao.addAll(lessons)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    Timber.e("Can't store lessons: ${e.message}")
                    finishJob(parameters)
                }
                override fun onComplete() {
                    transmitIdLessons(parameters)
                }
            }))
    }

    private fun transmitIdLessons(parameters: JobParameters) {
        disposables.add(lessonDao.getAllIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Long>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Timber.e("Can't load id lessons: ${e.message}")
                    finishJob(parameters)
                }
                override fun onNext(result: List<Long>) {
                    val intent = Intent(MediateReceiver.LESSON_IDS_ACTION)
                    intent.putExtra(MediateReceiver.LESSON_IDS_EXTRA, result.toLongArray())
                    sendBroadcast(intent)
                }
            }))
    }
}
