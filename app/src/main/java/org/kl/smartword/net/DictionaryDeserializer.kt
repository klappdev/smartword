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
package org.kl.smartword.net

import timber.log.Timber
import com.google.gson.*
import java.lang.reflect.Type

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.kl.smartword.model.Language

import org.kl.smartword.model.RemoteWord
import org.kl.smartword.model.Word

class DictionaryDeserializer : JsonDeserializer<Word> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?,
                             typeOf: Type?,
                             context: JsonDeserializationContext?): Word {
        val rawPages = json?.asJsonObject?.get("query")?.asJsonObject?.get("pages")

        /**
         FIXME: maybe using another approach
         val jsonObject = json?.asJsonObject ?: throw JsonParseException("Word json object is incorrect") 
        */

        if (rawPages != null) {
            val entries = rawPages.asJsonObject.entrySet()

            if (entries.isNotEmpty()) {
                val rawWord = Gson().fromJson(entries.first().value, RemoteWord::class.java)

                if (rawWord.content.isEmpty()) {
                    throw JsonParseException("Doesn't exist word ${rawWord.name}")
                }

                return parseRemoteWord(rawWord)
            }
        }

        return Word()
    }

    private fun parseRemoteWord(rawWord: RemoteWord): Word {
        Timber.d("Remote word response: $rawWord")

        var etymologyText = ""
        var translationText = ""
        var pluralsText = ""

        val language = Language.FRENCH
        val document: Document = Jsoup.parse(rawWord.content)

        val languageElement: Element? = document.selectFirst("h2 > span[id='${language.fullName}']")

        if (languageElement == null || languageElement.text().isEmpty()) {
            throw JsonParseException("Doesn't support language ${language.fullName}")
        }

        val etymologyElement: Element? = languageElement.parent()?.nextElementSibling()

        if (etymologyElement?.child(0)?.text() == "Etymology") {
            etymologyText = etymologyElement.nextElementSibling()?.text().toString()

            Timber.d("etymology text: $etymologyText")
        }

        val translationElement: Element? = document.selectFirst("ol > li")
        translationText = translationElement?.text().toString()

        Timber.d("translation text: $translationText")


        val pluralsElement: Element? = document.selectFirst("b[lang='${language.shortName}']")
        pluralsText = pluralsElement?.text().toString()

        Timber.d("plurals text: $pluralsText")


        /**
         * FIXME:
         * -1) parse transcription
         * 0) find - noun, verb ...
         * 1) add test - 123, lapin, dog
         * 2) word remove:  otherForm, antonym, irregular
         * 3) word add: description
         * 4) redesign with changes word
         */

        return Word(1, 1, rawWord.name, "[]", translationText, "", etymologyText, "")
    }
}
