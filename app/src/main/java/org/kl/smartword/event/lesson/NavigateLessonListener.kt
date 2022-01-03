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
package org.kl.smartword.event.lesson

import android.content.Intent
import android.view.View
import android.widget.AdapterView

import org.kl.smartword.ui.lesson.AddLessonActivity
import org.kl.smartword.ui.lesson.EditLessonActivity
import org.kl.smartword.ui.lesson.ShowLessonActivity
import org.kl.smartword.ui.lesson.DictionaryFragment

class NavigateLessonListener(dictionaryFragment: DictionaryFragment) {
    private val dictionaryAdapter = dictionaryFragment.dictionaryAdapter
    private val context = dictionaryAdapter.context

    fun navigateAddLesson(view: View?) {
        val intent = Intent(context, AddLessonActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateEditLesson(): Boolean {
        val idLesson: Long = dictionaryAdapter.getCurrentItemId()

        val intent = Intent(context, EditLessonActivity::class.java)
        intent.putExtra("id_lesson", idLesson)

        context.startActivity(intent)
        return true
    }

    fun navigateShowLesson(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(context, ShowLessonActivity::class.java)
        intent.putExtra("id_lesson", dictionaryAdapter.getItemId(position))

        context.startActivity(intent)
    }
}