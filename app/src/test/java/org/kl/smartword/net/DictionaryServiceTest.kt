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
package org.kl.smartword.net

import android.content.Context
import com.google.gson.JsonParser

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers

import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.net.URL

import org.mockito.Mock
import org.mockito.Matchers.anyMapOf
import org.mockito.Matchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

import org.kl.smartword.model.Word
import org.kl.smartword.util.RxSchedulerRule
import org.kl.smartword.util.readFileLine

@Config(sdk= [29])
@RunWith(RobolectricTestRunner::class)
class DictionaryServiceTest {
    @get:Rule
    public val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    public val rxScheduleRule = RxSchedulerRule()

    @Mock
    private lateinit var service: DictionaryService

    companion object {
        @JvmStatic
        private var context: Context? = null

        @JvmStatic
        private var wordContentDeserializer: WordContentDeserializer? = null

        @JvmStatic
        private val wordImageDeserializer = WordImageDeserializer()

        @JvmStatic
        private val parser = JsonParser()

        @JvmStatic
        private val disposable = CompositeDisposable()

        @JvmStatic
        @AfterClass
        fun tearDown() {
            disposable.dispose()
        }
    }

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application
        wordContentDeserializer = WordContentDeserializer(context!!)
    }

    @Test
    fun `test get word content with multiple parameters`() {
        val word = Word(1L, 1L, "Hund", "[hund]", "dog", "-", "-", "-", "01.07.2022")
        val observer = TestObserver<Word>()

        `when`(service.getWordContent(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Observable.just(word))

        disposable.add(service.getWordContent("query", "extracts", "", "json", "", "Hund")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(1)

        val result = observer.values()[0]

        assertEquals(1L, result.idLesson)
        assertEquals("Hund", result.name)
        assertEquals("[hund]", result.transcription)
        assertEquals("dog", result.translation)
        assertEquals("-", result.association)
        assertEquals("-", result.etymology)
        assertEquals("-", result.description)
        assertEquals("01.07.2022", result.date)
    }

    @Test
    fun `test get word content with map parameters`() {
        val parameters = mapOf(
            "action" to "query",
            "prop" to "extracts",
            "redirects" to "",
            "format" to "json",
            "continue" to "",
            "titles" to "Katze"
        )

        val word = Word(1L, 1L, "Katze", "[katze]", "cat", "-", "-", "-", "02.07.2022")
        val observer = TestObserver<Word>()

        `when`(service.getWordContent(anyMapOf(String::class.java, String::class.java)))
            .thenReturn(Observable.just(word))

        disposable.add(service.getWordContent(parameters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))

        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(1)

        val result = observer.values()[0]

        assertEquals(1L, result.idLesson)
        assertEquals("Katze", result.name)
        assertEquals("[katze]", result.transcription)
        assertEquals("cat", result.translation)
        assertEquals("-", result.association)
        assertEquals("-", result.etymology)
        assertEquals("-", result.description)
        assertEquals("02.07.2022", result.date)
    }

    @Test
    fun `test parse first noun word`() {
        // https://en.wiktionary.org/w/api.php?format=json&action=query&prop=extracts&redirects&continue&titles=<title>
        val line: String = context?.assets?.readFileLine("first_noun_word.json")!!
        val result: Word = wordContentDeserializer?.deserialize(parser.parse(line), null, null)!!

        assertEquals(1L, result.idLesson)
        assertEquals("Katze", result.name)
        assertNotNull(result.transcription)
        assertNotNull(result.translation)
        assertNotNull(result.association)
        assertNotNull(result.etymology)
        assertNotNull(result.description)
        assertNotNull(result.date)
    }

    @Test
    fun `test parse second noun word`() {
        val line: String = context?.assets?.readFileLine("second_noun_word.json")!!
        val result: Word = wordContentDeserializer?.deserialize(parser.parse(line), null, null)!!

        assertEquals(1L, result.idLesson)
        assertEquals("Hund", result.name)
        assertNotNull(result.transcription)
        assertNotNull(result.translation)
        assertNotNull(result.association)
        assertNotNull(result.etymology)
        assertNotNull(result.description)
        assertNotNull(result.date)
    }


    @Test
    fun `test parse first image word`() {
        // https://en.wiktionary.org/w/api.php?format=json&action=query&prop=pageimages&piprop=original&redirects&continue&titles=<name>
        val line: String = context?.assets?.readFileLine("first_image_word.json")!!
        val result: URL = wordImageDeserializer.deserialize(parser.parse(line), null, null)

        assertEquals("https", result.protocol)
        assertEquals("upload.wikimedia.org", result.authority)
        assertEquals("/wikipedia/commons/6/60/YellowLabradorLooking.jpg", result.file)
    }

    @Test
    fun `test parse second image word`() {
        val line: String = context?.assets?.readFileLine("second_image_word.json")!!
        val result: URL = wordImageDeserializer.deserialize(parser.parse(line), null, null)

        assertEquals("https", result.protocol)
        assertEquals("upload.wikimedia.org", result.authority)
        assertEquals("/wikipedia/commons/3/3a/Cat03.jpg", result.file)
    }
}