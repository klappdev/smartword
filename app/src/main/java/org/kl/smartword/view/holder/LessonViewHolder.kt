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
package org.kl.smartword.view.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import org.kl.smartword.R
import org.kl.smartword.model.Lesson

class LessonViewHolder {
    private val nameTextView: TextView
    private val dateTextView: TextView
    private val itemImageView: ImageView

    constructor(view: View) {
        this.nameTextView = view.findViewById(R.id.name_lesson_text_view)
        this.dateTextView = view.findViewById(R.id.date_lesson_text_view)
        this.itemImageView = view.findViewById(R.id.item_lesson_image)
    }

    fun bind(item: Lesson, currentId: Long) {
        if (item.id == currentId) {
            itemImageView.setImageResource(R.drawable.lesson_selected_icon)
        } else {
            itemImageView.setImageResource(R.drawable.lesson_icon)
        }

        nameTextView.text = item.name
        dateTextView.text = item.date
    }
}