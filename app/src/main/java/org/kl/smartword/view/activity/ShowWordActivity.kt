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

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.button.MaterialButton
import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.WordApplication
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word

class ShowWordActivity : AppCompatActivity() {
    private lateinit var nameWordTextView: TextView
    private lateinit var dateWordTextView: TextView
    private lateinit var transcriptionTextView: TextView
    private lateinit var translationTextView: TextView
    private lateinit var associationTextView: TextView
    private lateinit var etymologyTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var nextWordButton: MaterialButton

    @Inject
    public lateinit var wordDao: WordDao

    @Inject
    public lateinit var disposables: CompositeDisposable
    private var idWord: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WordApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_word)

        initView()
        initWord()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        this.nameWordTextView = findViewById(R.id.name_word_text_view)
        this.dateWordTextView = findViewById(R.id.date_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)
        this.descriptionTextView = findViewById(R.id.description_word_text_view)
        this.nextWordButton = findViewById(R.id.next_word_button)

        idWord = intent.getLongExtra("id_word", -1)
    }

    private fun initWord() {
        disposables.add(wordDao.getById(idWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: Word ->
                nameWordTextView.text = result.name
                dateWordTextView.text = result.date
                transcriptionTextView.text = result.transcription
                translationTextView.text = result.translation
                associationTextView.text = result.association
                etymologyTextView.text = result.etymology
                descriptionTextView.text = result.description
            })
    }
}