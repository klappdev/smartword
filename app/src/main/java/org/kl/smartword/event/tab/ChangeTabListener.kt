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
package org.kl.smartword.event.tab

import android.content.Context
import androidx.viewpager.widget.ViewPager
import timber.log.Timber

import org.kl.smartword.view.adapter.CATEGORY_TAB
import org.kl.smartword.view.adapter.DICTIONARY_TAB
import org.kl.smartword.view.adapter.SectionPagerAdapter
import org.kl.smartword.view.fragment.DictionaryFragment
import org.kl.smartword.view.fragment.CategoryFragment

class ChangeTabListener(private val pageAdapter: SectionPagerAdapter,
                        private val context: Context) : ViewPager.OnPageChangeListener {

    override fun onPageSelected(position: Int) {
        Timber.d("current tab selected: $position")

        when (position) {
            CATEGORY_TAB -> {
                val fragment = pageAdapter.getItem(position) as CategoryFragment

                /*fragment.updateCategoryContents(context, adapter)*/
            }
            DICTIONARY_TAB -> {
                val fragment = pageAdapter.getItem(position) as DictionaryFragment

                /*fragment.updateDictionaryContents(context, adapter)*/
            }
            else -> error("Unknown tab order")
        }
    }

    override fun onPageScrollStateChanged(position: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}