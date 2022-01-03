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

import com.google.gson.*
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.net.URL

import org.kl.smartword.model.RemoteImage
import org.kl.smartword.model.RemoteWord

class WordImageDeserializer : JsonDeserializer<URL> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?,
                             typeOf: Type?,
                             context: JsonDeserializationContext?): URL {
        val rawPages = json?.asJsonObject?.get("query")?.asJsonObject?.get("pages")

        if (rawPages != null) {
            val entries = rawPages.asJsonObject.entrySet()

            if (entries.isNotEmpty()) {
                val wordType: Type = object : TypeToken<RemoteWord<JsonElement>>(){}.type
                val rawWord: RemoteWord<JsonElement> = Gson().fromJson(entries.first().value, wordType)

                if (rawWord.content.isJsonNull) {
                    throw JsonParseException("Doesn't exist image ${rawWord.name}")
                }

                val rawImage = Gson().fromJson(rawWord.content, RemoteImage::class.java)
                return URL(rawImage.url)
            }
        }

        return URL("http://unknown.com")
    }
}