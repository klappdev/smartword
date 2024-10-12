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
package org.kl.smartword.di

import dagger.Component
import org.kl.smartword.ui.MainActivity
import javax.inject.Singleton

import org.kl.smartword.ui.lesson.CategoryFragment
import org.kl.smartword.ui.lesson.AddLessonActivity
import org.kl.smartword.ui.lesson.DictionaryFragment
import org.kl.smartword.ui.lesson.EditLessonActivity
import org.kl.smartword.ui.lesson.ShowLessonActivity
import org.kl.smartword.ui.settings.SettingsFragment
import org.kl.smartword.ui.word.AddWordActivity
import org.kl.smartword.ui.word.EditWordActivity
import org.kl.smartword.ui.word.ShowWordActivity
import org.kl.smartword.work.LoadLessonService
import org.kl.smartword.work.LoadWordService

@Singleton
@Component(modules = [
    AppModule::class, BackgroundModule::class,
    DatabaseModule::class, NetworkModule::class
])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: AddLessonActivity)
    fun inject(activity: EditLessonActivity)
    fun inject(activity: ShowLessonActivity)
    fun inject(activity: AddWordActivity)
    fun inject(activity: EditWordActivity)
    fun inject(activity: ShowWordActivity)

    fun inject(fragment: CategoryFragment)
    fun inject(fragment: DictionaryFragment)
    fun inject(fragment: SettingsFragment)

    fun inject(service: LoadLessonService)
    fun inject(service: LoadWordService)
}