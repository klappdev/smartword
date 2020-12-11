package org.kl.smartword.event.lesson

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
import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.view.MainActivity
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.fragment.DictionaryFragment

class SearchLessonListener : View.OnClickListener, MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener {
    private val activity: MainActivity
    private val disposables: CompositeDisposable
    private val dictionaryAdapter: DictionaryAdapter

    private var searchView: SearchView? = null
    private var searchInput: TextView? = null
    private var closeIcon: ImageView? = null
    private var currentSize: Int = -1

    constructor(dictionaryFragment: DictionaryFragment) {
        this.activity = dictionaryFragment.activity as MainActivity
        this.disposables = activity.disposables

        this.dictionaryAdapter = dictionaryFragment.dictionaryAdapter
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            disposables.add(LessonDao.searchByName(newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(result: List<Lesson>) {
                        dictionaryAdapter.listLessons.clear()
                        dictionaryAdapter.listLessons.addAll(result)
                        dictionaryAdapter.notifyDataSetChanged()

                        currentSize = result.size
                    }
                }))
        } else {
            refreshLessons()
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
        dictionaryAdapter.position = -1
        activity.notifyMenuItemSelected(false)
        refreshLessons()

        return true
    }

    override fun onClick(view: View?) {
        refreshLessons()
        this.searchInput?.text = ""
    }

    private fun refreshLessons() {
        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    if (currentSize != -1 && currentSize != result.size) {
                        dictionaryAdapter.listLessons.clear()
                        dictionaryAdapter.listLessons.addAll(result)
                        dictionaryAdapter.notifyDataSetChanged()
                        currentSize = -1
                    }
                }
            }))
    }
}