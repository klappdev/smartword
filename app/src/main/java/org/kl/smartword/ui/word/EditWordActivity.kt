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
package org.kl.smartword.ui.word

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import butterknife.BindView
import butterknife.ButterKnife

import org.kl.smartword.R
import org.kl.smartword.MainApplication
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.model.Word
import org.kl.smartword.event.word.EditWordListener

class EditWordActivity : AppCompatActivity() {
    @BindView(R.id.name_word_text_view)
    public lateinit var nameTextView: TextView
    @BindView(R.id.transcription_word_text_view)
    public lateinit var transcriptionTextView: TextView
    @BindView(R.id.translation_word_text_view)
    public lateinit var translationTextView: TextView
    @BindView(R.id.association_word_text_view)
    public lateinit var associationTextView: TextView
    @BindView(R.id.etymology_word_text_view)
    public lateinit var etymologyTextView: TextView
    @BindView(R.id.description_word_text_view)
    public lateinit var descriptionTextView: TextView
    @BindView(R.id.edit_word_button)
    public lateinit var editButton: Button

    @Inject
    public lateinit var wordDao: WordDao
    @Inject
    public lateinit var viewValidator: ViewValidator
    @Inject
    public lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_word)
        ButterKnife.bind(this)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        val idWord = intent.getLongExtra("id_word", -1)

        disposables.add(wordDao.getById(idWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: Word ->
                nameTextView.text = result.name
                transcriptionTextView.text = result.transcription
                translationTextView.text = result.translation
                associationTextView.text = result.association
                etymologyTextView.text = result.etymology
                descriptionTextView.text = result.description

                val listener = EditWordListener(this@EditWordActivity, result.id, result.idLesson)
                editButton.setOnClickListener(listener)
            })
    }
}
