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
package org.kl.smartword.ui.lesson

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import javax.inject.Inject
import io.reactivex.rxjava3.disposables.CompositeDisposable

import butterknife.BindView
import butterknife.ButterKnife

import org.kl.smartword.R
import org.kl.smartword.MainApplication
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.lesson.AddLessonListener
import org.kl.smartword.event.validate.ViewValidator

class AddLessonActivity : AppCompatActivity() {
    @BindView(R.id.name_lesson_text_view)
    public lateinit var nameTextView: TextView
    @BindView(R.id.description_lesson_text_view)
    public lateinit var descriptionTextView: TextView
    @BindView(R.id.add_lesson_button)
    public lateinit var addButton: Button

    @Inject
    public lateinit var lessonDao: LessonDao
    @Inject
    public lateinit var viewValidator: ViewValidator
    @Inject
    public lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lesson)
        ButterKnife.bind(this)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        addButton.setOnClickListener(AddLessonListener(this))
    }
}

