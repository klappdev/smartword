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
package org.kl.smartword.util

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.stream.JsonReader

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import java.lang.reflect.Type
import java.nio.charset.Charset

@Throws(Exception::class)
fun <T> AssetManager.readJsonFileLines(name: String, type: Type): List<T> {
    if (!name.endsWith(".json")) {
        throw Exception("File hasn't .json extension")
    }

    val lines = mutableListOf<T>()

    this.open(name).use { inputStream ->
        JsonReader(inputStream.reader()).use { jsonReader ->
            lines.addAll(Gson().fromJson(jsonReader, type))
        }
    }

    return lines
}

@Throws(Exception::class)
fun AssetManager.readFileLines(name: String, charsets: Charset = StandardCharsets.UTF_8): List<String> {
    val lines = mutableListOf<String>()

    this.open(name).use { inputStream ->
        BufferedReader(InputStreamReader(inputStream, charsets)).use { bufferReader ->
            lines.addAll(bufferReader.lines().collect(Collectors.toList()))
        }
    }

    return lines
}
@Throws(Exception::class)
fun AssetManager.readFileLine(name: String, delimiter: String = " "): String {
    return this.readFileLines(name).joinToString(delimiter)
}
