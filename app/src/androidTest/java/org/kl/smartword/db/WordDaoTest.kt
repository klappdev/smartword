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
package org.kl.smartword.db

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
import org.kl.smartword.model.Word

@RunWith(AndroidJUnit4::class)
class WordDaoTest {
    @get:Rule
    public val rxScheduleRule = RxAndroidSchedulerRule()

    @Inject
    public lateinit var dbHelper: DatabaseHelper
    @Inject
    public lateinit var wordDao: WordDao
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
        dbHelper.close()
        disposable.dispose()
    }

    @Test
    fun testAddWord() {
        val name = "first word"
        val word = Word(1L, 1L, name, "", "", "", "", "", "02.07.2022")
        val observer = TestObserver<Unit>()

        disposable.add(wordDao.add(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()

        val result: List<Word> = getByNameWords(name)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertNotNull(result[0].name)
        assertNotNull(result[0].transcription)
        assertNotNull(result[0].translation)
        assertNotNull(result[0].association)
        assertNotNull(result[0].etymology)
        assertNotNull(result[0].description)
        assertNotNull(result[0].date)
    }

    @Test
    fun testUpdateWord() {
        val name = "second word"
        val word = Word(2L, 1L, name, "", "", "", "", "", "02.07.2022")
        val observer = TestObserver<Unit>()

        disposable.add(wordDao.update(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()

        val result: List<Word> = getByNameWords(name)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertNotNull(result[0].name)
        assertNotNull(result[0].transcription)
        assertNotNull(result[0].translation)
        assertNotNull(result[0].association)
        assertNotNull(result[0].etymology)
        assertNotNull(result[0].description)
        assertNotNull(result[0].date)
    }

    @Test
    fun testDeleteWord() {
        val observer = TestObserver<Unit>()

        disposable.add(wordDao.delete(1L)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()
    }

    private fun getByNameWords(name: String): List<Word> {
        val observer = TestObserver<List<Word>>()

        disposable.add(wordDao.searchByName(name, 1L)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(1)

        return observer.values()[0]
    }
}