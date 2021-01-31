package org.kl.smartword.event.word

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word
import org.kl.smartword.view.ShowLessonActivity
import org.kl.smartword.view.adapter.LessonAdapter

class SearchWordListener : View.OnClickListener, MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener {
    private val activity: ShowLessonActivity
    private val disposables: CompositeDisposable
    private val lessonAdapter: LessonAdapter

    private var searchView: SearchView? = null
    private var searchInput: TextView? = null
    private var closeIcon: ImageView? = null
    private var currentSize: Int = -1

    constructor(activity: ShowLessonActivity, lessonAdapter: LessonAdapter, disposables: CompositeDisposable) {
        this.activity = activity
        this.lessonAdapter = lessonAdapter
        this.disposables = disposables
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            disposables.add(WordDao.searchByName(newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<List<Word>>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(result: List<Word>) {
                        lessonAdapter.listWords.clear()
                        lessonAdapter.listWords.addAll(result)
                        lessonAdapter.notifyDataSetChanged()

                        currentSize = result.size
                    }
                }))
        } else {
            refreshWords()
        }

        return true
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onMenuItemActionExpand(view: MenuItem?): Boolean {
        if (searchView == null) {
            searchView = view?.actionView as SearchView
            searchView?.queryHint = activity.getString(R.string.search_hint)
            searchView?.setOnQueryTextListener(this)

            this.searchInput = searchView?.findViewById(androidx.appcompat.R.id.search_src_text)
            this.closeIcon = searchView?.findViewById(androidx.appcompat.R.id.search_close_btn)
            closeIcon?.setOnClickListener(this)
        }

        return true
    }

    override fun onMenuItemActionCollapse(view: MenuItem?): Boolean {
        lessonAdapter.position = -1
        activity.notifyMenuItemSelected(false)
        refreshWords()

        return true
    }

    override fun onClick(view: View?) {
        refreshWords()
        this.searchInput?.text = ""
    }

    private fun refreshWords() {
        disposables.add(WordDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    if (currentSize != -1 && currentSize != result.size) {
                        lessonAdapter.listWords.clear()
                        lessonAdapter.listWords.addAll(result)
                        lessonAdapter.notifyDataSetChanged()
                        currentSize = -1
                    }
                }
            }))
    }
}