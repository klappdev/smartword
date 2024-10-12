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
package org.kl.smartword.ui.lesson

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

import butterknife.BindView
import butterknife.Unbinder
import butterknife.ButterKnife

import org.kl.smartword.R
import org.kl.smartword.MainApplication
import org.kl.smartword.db.LessonDao
import org.kl.smartword.model.Lesson
import org.kl.smartword.ui.MainActivity

class CategoryFragment : Fragment() {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var unbinder: Unbinder

    @BindView(R.id.category_empty_text_view)
    public lateinit var emptyTextView: TextView
    @BindView(R.id.category_recycle_view)
    public lateinit var categoryRecyclerView: RecyclerView

    @Inject
    public lateinit var lessonDao: LessonDao
    @Inject
    public lateinit var disposables: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)
        setHasOptionsMenu(true)
        unbinder = ButterKnife.bind(this, rootView)

        initView()
        updateLessons()

        return rootView
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as MainApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        initLessons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
        unbinder.unbind()
    }

    private fun initView() {
        this.categoryAdapter = CategoryAdapter(mutableListOf())

        val mainActivity = (activity as MainActivity)
        initListeners(mainActivity)

        with(categoryRecyclerView) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoryAdapter
        }
    }

    private fun initListeners(activity: MainActivity) {
        activity.categoryFragment = this

        /*TODO: init listeners*/
    }

    private fun initLessons() {
        disposables.add(lessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: List<Lesson> ->
                categoryAdapter.listLessons.clear()
                categoryAdapter.listLessons.addAll(result)
                categoryAdapter.notifyDataSetChanged()

                switchVisibility(result.isNotEmpty())
            })
    }

    private fun updateLessons() {
        disposables.add(lessonDao.dataSubject.subscribe { result: List<Lesson> ->
            categoryAdapter.listLessons.clear()
            categoryAdapter.listLessons.addAll(result)
            categoryAdapter.notifyDataSetChanged()

            switchVisibility(result.isNotEmpty())
        })
    }

    private fun switchVisibility(flag: Boolean) {
        if (flag) {
            categoryRecyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE

        } else {
            categoryRecyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        }
    }
}