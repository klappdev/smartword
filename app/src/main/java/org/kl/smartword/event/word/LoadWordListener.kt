package org.kl.smartword.event.word

import android.view.View
import timber.log.Timber

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.view.activity.AddWordActivity
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