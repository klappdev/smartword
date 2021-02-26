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

import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import butterknife.BindView
import butterknife.ButterKnife

import org.kl.smartword.R
import org.kl.smartword.model.Lesson

class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    @BindView(R.id.category_card_view)
    lateinit var categoryCardView: CardView
    @BindView(R.id.item_category_image)
    lateinit var categoryImage: ImageView
    @BindView(R.id.name_text_view)
    lateinit var nameTextView: TextView

    init {
        ButterKnife.bind(this, view)
    }

    fun bind(item: Lesson) {
        nameTextView.text = item.name

        Glide.with(categoryImage.context)
            .load(item.iconUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(categoryImage)
    }
}