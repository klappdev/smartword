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
package org.kl.smartword.event.word

import android.content.Intent
import android.view.View
import android.widget.AdapterView

import org.kl.smartword.ui.word.AddWordActivity
import org.kl.smartword.ui.word.EditWordActivity
import org.kl.smartword.ui.word.ShowWordActivity
import org.kl.smartword.ui.lesson.LessonAdapter

class NavigateWordListener(private val lessonAdapter: LessonAdapter, private val idLesson: Long) {
    private val context = lessonAdapter.context

    fun navigateAddWord(view: View?) {
        val intent = Intent(context, AddWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_lesson", idLesson)

        context.startActivity(intent)
    }

    fun navigateEditWord(): Boolean {
        val idWord: Long = lessonAdapter.getCurrentItemId()

        val intent = Intent(context, EditWordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("id_word", idWord)

        context.startActivity(intent)
        return true
    }

    fun navigateShowWord(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(context, ShowWordActivity::class.java)
        intent.putExtra("id_word", lessonAdapter.getItemId(position))

        context.startActivity(intent)
    }
}