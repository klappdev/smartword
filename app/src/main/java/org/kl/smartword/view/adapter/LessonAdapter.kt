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
package org.kl.smartword.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import org.kl.smartword.R
import org.kl.smartword.model.Word
import org.kl.smartword.view.holder.WordViewHolder

class LessonAdapter(val context: Context, val listWords: MutableList<Word>) : BaseAdapter() {
    var position: Int = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val word = getItem(position)
        val holder: WordViewHolder

        if (view == null) {
            view = LayoutInflater.from(context)
                                 .inflate(R.layout.word_item, parent, false)
            holder = WordViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as WordViewHolder
        }

        holder.bind(word, getCurrentItemId())

        return view
    }

    override fun getCount() = listWords.size
    override fun getItem(position: Int) = listWords[position]
    override fun getItemId(position: Int) = listWords[position].id

    fun getCurrentItem() = getItem(position)
    fun getCurrentItemId() = if (position != -1 && position < count) getItemId(position) else -1
}