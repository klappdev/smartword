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
import android.widget.Button
import android.widget.TextView
import javax.inject.Inject

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.CompositeDisposable

import org.kl.smartword.R
import org.kl.smartword.WordApplication
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.event.word.AddWordListener
import org.kl.smartword.event.word.LoadWordListener
import org.kl.smartword.net.DictionaryService
import org.kl.smartword.net.NetworkConnectivityHelper

class AddWordActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    private lateinit var loadButton: FloatingActionButton

    lateinit var nameTextView: TextView
        private set
    lateinit var transcriptionTextView: TextView
        private set
    lateinit var translationTextView: TextView
        private set
    lateinit var associationTextView: TextView
        private set
    lateinit var etymologyTextView: TextView
        private set
    lateinit var descriptionTextView: TextView
        private set

    @Inject
    public lateinit var wordDao: WordDao

    @Inject
    public lateinit var networkConnectivity: NetworkConnectivityHelper

    @Inject
    public lateinit var dictionaryService: DictionaryService

    @Inject
    public lateinit var viewValidator: ViewValidator

    @Inject
    public lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WordApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        this.nameTextView = findViewById(R.id.name_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)

        this.addButton = findViewById(R.id.add_word_button)
        this.loadButton = findViewById(R.id.load_word_button)

        val idLesson = intent.getLongExtra("id_lesson", -1)
        addButton.setOnClickListener(AddWordListener(this, idLesson))

        loadButton.setOnClickListener(LoadWordListener(this))
    }
}
