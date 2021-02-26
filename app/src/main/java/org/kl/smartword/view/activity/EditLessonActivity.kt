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
package org.kl.smartword.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.WordApplication
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.lesson.EditLessonListener
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.model.Lesson

class EditLessonActivity : AppCompatActivity() {
    private lateinit var editButton: Button
    public  lateinit var nameTextView: TextView
        private set
    public  lateinit var descriptionTextView: TextView
        private set

    @Inject
    public lateinit var viewValidator: ViewValidator

    @Inject
    public lateinit var lessonDao: LessonDao

    @Inject
    public lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WordApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lesson)

        initView()
        initLesson()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        this.nameTextView = findViewById(R.id.name_lesson_text_view)
        this.descriptionTextView = findViewById(R.id.description_lesson_text_view)
        this.editButton = findViewById(R.id.edit_lesson_button)
    }

    private fun initLesson() {
        val idLesson = intent.getLongExtra("id_lesson", -1)

        disposables.add(lessonDao.getById(idLesson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableMaybeObserver<Lesson>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {}
                    override fun onSuccess(result: Lesson) {
                        nameTextView.text = result.name
                        descriptionTextView.text = result.description
                    }
                }))

        editButton.setOnClickListener(EditLessonListener(this, idLesson))
    }
}
