package org.kl.smartword.work

import android.util.Log
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.Context
import android.content.ComponentName

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.reactivex.Observable

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import java.util.concurrent.TimeUnit

class LoadInitDBService : JobService() {
	private val disposables = CompositeDisposable()
	private var restartServiceOnDestroy = true
	
	companion object {
		private const val TAG = "LILS-TAG"
        private const val JOB_ID = 1

        fun scheduleJob(context: Context) {
			val componentName = ComponentName(context, LoadInitDBService::class.java)
			val jobInfo = JobInfo.Builder(JOB_ID, componentName)
                .setMinimumLatency(TimeUnit.SECONDS.toMillis(1))
                .setOverrideDeadline(TimeUnit.SECONDS.toMillis(5))
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .build()
			
			val schedulerService = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            schedulerService.schedule(jobInfo)
        }

        fun cancelJob(context: Context) {
			val schedulerService = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            schedulerService.cancel(JOB_ID)
        }
    }

	override fun onStartJob(parameters: JobParameters): Boolean {
		Log.i(TAG, "Start load init lesson job: ${parameters.jobId}")	

        /*FIXME: first of all check if db empty*/
		disposables.add(Observable.fromCallable(::loadLessons)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
					addLessons(result, parameters)
                }
            }))
		
		return true;
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
	
	private fun addLessons(lessons: List<Lesson>, parameters: JobParameters) {
        disposables.add(LessonDao.addAll(lessons)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
					Log.e(TAG, "Can't add lesson: ${e.message}")
                }
                override fun onComplete() {
					finishJob(parameters)
					
					Log.i(TAG, "Finish load lessons success")
                }
            }))
    }
	
	private fun loadLessons(): List<Lesson> {
		val lessons = mutableListOf<Lesson>()
		
		try {
            applicationContext.assets.open("lessons.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val lessonType = object : TypeToken<List<Lesson>>(){}.type
                    lessons.addAll(Gson().fromJson(jsonReader, lessonType))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error preload data to db from json", e)            
        }
		
		return lessons;
	}	
}