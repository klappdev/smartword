/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2024 https://github.com/klappdev
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
package org.kl.smartword.event.settings

import androidx.preference.Preference

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import org.kl.smartword.settings.CommonSettings
import org.kl.smartword.ui.settings.SettingsFragment
import org.kl.smartword.util.toast

class ChangeThemeListener(fragment: SettingsFragment) : Preference.OnPreferenceChangeListener {
    private val context = fragment.context
    private val disposable = fragment.disposable

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (context == null) return false

        val settings = CommonSettings.getInstance(context)
        val themes = settings.availableThemes
        val newTheme = if (newValue as Boolean) themes[0] else themes[1]

        disposable.add(settings.writeTheme(newTheme)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    context.toast("Changed theme: $newTheme")
                }
                override fun onError(e: Throwable) {
                    context.toast("Can't change theme: $newTheme")
                }
            }))

        return true
    }
}
