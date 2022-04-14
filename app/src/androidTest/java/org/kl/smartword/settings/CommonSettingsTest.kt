/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2022 https://github.com/klappdev
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
package org.kl.smartword.settings

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers

import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.kl.smartword.TestMainApplication
import javax.inject.Inject

import org.kl.smartword.di.TestAppComponent
import org.kl.smartword.util.RxAndroidSchedulerRule

@RunWith(AndroidJUnit4::class)
class CommonSettingsTest {

    @get:Rule
    public val rxScheduleRule = RxAndroidSchedulerRule()

    @Inject
    public lateinit var settings: CommonSettings
    @Inject
    public lateinit var disposable: CompositeDisposable

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val application = context.applicationContext as TestMainApplication
        application.initializeDagger()
        (application.appComponent as TestAppComponent).inject(this)
    }

    @After
    fun tearDown() {
        disposable.dispose()
    }

    @Test
    fun testWriteAndReadLanguage() {
        val writeObserver = TestObserver<Unit>()
        val expectedLanguage = "English"

        disposable.add(settings.writeLanguage(expectedLanguage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(writeObserver))

        writeObserver.assertComplete()
        writeObserver.assertNoErrors()

        val readObserver = TestObserver<String>()

        disposable.add(settings.readLanguage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(readObserver))

        readObserver.assertComplete()
        readObserver.assertNoErrors()
        readObserver.assertValueCount(1)

        val actualLanguage = readObserver.values()[0]

        assertEquals(expectedLanguage, actualLanguage)
    }

    @Test
    fun testWriteAndReadTheme() {
        val writeObserver = TestObserver<Unit>()
        val expectedTheme = "Light"

        disposable.add(settings.writeTheme(expectedTheme)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(writeObserver))

        writeObserver.assertComplete()
        writeObserver.assertNoErrors()

        val readObserver = TestObserver<String>()

        disposable.add(settings.readTheme()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(readObserver))

        readObserver.assertComplete()
        readObserver.assertNoErrors()
        readObserver.assertValueCount(1)

        val actualTheme = readObserver.values()[0]

        assertEquals(expectedTheme, actualTheme)
    }
}