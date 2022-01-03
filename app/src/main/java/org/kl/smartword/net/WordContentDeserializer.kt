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
package org.kl.smartword.net

import android.content.Context
import timber.log.Timber
import java.lang.reflect.Type

import com.google.gson.*
import com.google.gson.reflect.TypeToken

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import org.kl.smartword.model.RemoteWord
import org.kl.smartword.model.Word
import org.kl.smartword.settings.CommonSettings

class WordContentDeserializer(private val context: Context) : JsonDeserializer<Word> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?,
                             typeOf: Type?,
                             context: JsonDeserializationContext?): Word {
        val rawPages = json?.asJsonObject?.get("query")?.asJsonObject?.get("pages")

        if (rawPages != null) {
            val entries = rawPages.asJsonObject.entrySet()

            if (entries.isNotEmpty()) {
                val wordType: Type = object : TypeToken<RemoteWord<String>>(){}.type
                val rawWord: RemoteWord<String> = Gson().fromJson(entries.first().value, wordType)

                if (rawWord.content.isEmpty()) {
                    throw JsonParseException("Doesn't exist word ${rawWord.name}")
                }

                return parseRemoteWord(rawWord)
            }
        }

        return Word()
    }

    private fun parseRemoteWord(remoteWord: RemoteWord<String>): Word {
        val document: Document = Jsoup.parse(parseLanguage(remoteWord.content))

        Timber.d("Start parse word: ${remoteWord.name}!")

        val etymologyText = parseEtymology(document)
        val transcriptionText = parseTranscription(document)
        val translationText = parseTranslation(document)
        val descriptionText = parseDescription(document)
        val associationText = parseAssociation(document)

        Timber.d("End parse word!")

        return Word(1L, 1L, remoteWord.name, transcriptionText, translationText, associationText,
                    etymologyText, descriptionText)
    }

    private fun parseLanguage(content: String): String {
        val availableLanguage: String = CommonSettings.getInstance(context).availableLanguages[1]
        val contentLanguages: List<String> = content.split("<h2><span id=")

        return when (contentLanguages.size - 1) {
            0 -> throw JsonParseException("Doesn't support language $availableLanguage")
            1 -> content
            else -> contentLanguages.first { it in availableLanguage }
        }
    }

    private fun parseEtymology(document: Document): String {
        var result = ""

        val etymologyTitleElement: Element? = document.selectFirst("h3 > span[id*='Etymology']")

        if (etymologyTitleElement?.text()?.isNotEmpty() == true) {
            val etymologyContentElement: Element? = etymologyTitleElement.parent().nextElementSibling()

            if (etymologyContentElement != null && etymologyContentElement.tagName() == "p") {
                result = etymologyContentElement.text()
            }
        }

        Timber.d("Etymology: $result")

        return result
    }

    private fun parseTranscription(document: Document): String {
        var result = ""

        val transcriptionTitleElement: Element? = document.selectFirst("h3 > span[id*='Pronunciation']")

        if (transcriptionTitleElement?.text()?.isNotEmpty() == true) {
            val transcriptionContentElement: Element? = transcriptionTitleElement.parent().nextElementSibling()

            if (transcriptionContentElement != null && transcriptionContentElement.tagName() == "ul") {
                result = transcriptionContentElement.text()
            }
        }

        Timber.d("Transcription: $result")

        return result
    }

    private fun parseTranslation(document: Document): String {
        var result = ""

        val translationElement: Element? = document.selectFirst("ol")

        if (translationElement != null && translationElement.children().isNotEmpty()) {
            result = translationElement.text()
        }

        Timber.d("Translation: $result")

        return result
    }

    private fun parseDescription(document: Document): String {
        val partsOfSpeeches: Array<String> = CommonSettings.getInstance(context).availablePartsOfSpeech
        var result = ""

        for (partOfSpeech in partsOfSpeeches) {
            val partOfSpeechElement: Element? = document.selectFirst("h3 > span[id*='$partOfSpeech']")

            if (partOfSpeechElement != null && partOfSpeechElement.parent().nextElementSibling().tagName() == "p") {
                result = partOfSpeechElement.parent().nextElementSibling().text()
            }
        }

        Timber.d("Description: $result")

        return result
    }

    private fun parseAssociation(document: Document): String {
        val builder = StringBuilder("")

        builder.append(parseAntonyms(document))
        builder.append(parseSynonyms(document))

        Timber.d("Association: $builder")

        return builder.toString()
    }

    private fun parseAntonyms(document: Document): String {
        val builder = StringBuilder("")

        val antonymsTitleElement: Element? = document.selectFirst("h4 > span[id='Antonyms']")

        val highAntonymsContentElement: Element? = antonymsTitleElement?.parent()?.nextElementSibling()

        if (highAntonymsContentElement?.tagName()?.equals("p") == true) {
            builder.append("antonyms: ")
            builder.append(highAntonymsContentElement.text())
        }

        val lowAntonymsContentElement: Element? = highAntonymsContentElement?.nextElementSibling()

        if (lowAntonymsContentElement?.tagName()?.equals("ul") == true) {
            builder.append(lowAntonymsContentElement.text())
        }

        return builder.toString()
    }

    private fun parseSynonyms(document: Document): String {
        val builder = StringBuilder("")

        val firstSynonymsTitleElement: Element? = document.selectFirst("h3 > span[id='Synonyms']")
        val secondSynonymsTitleElement: Element? = document.selectFirst("h4 > span[id='Synonyms']")

        var synonymsContentElement: Element? = firstSynonymsTitleElement?.parent()?.nextElementSibling()
        if (synonymsContentElement?.tagName()?.equals("ul") == true) {
            builder.append("synonyms: ")
            builder.append(synonymsContentElement.text())
        }

        synonymsContentElement = secondSynonymsTitleElement?.parent()?.nextElementSibling()
        if (synonymsContentElement?.tagName()?.equals("ul") == true) {
            builder.append("synonyms: ")
            builder.append(synonymsContentElement.text())
        }

        return builder.toString()
    }
}
