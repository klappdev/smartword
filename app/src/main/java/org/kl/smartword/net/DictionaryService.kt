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

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

import io.reactivex.Observable
import org.kl.smartword.model.Word
import java.net.URL

interface DictionaryService {

    @GET("w/api.php")
    fun getWordContent(@Query("action") action: String,
                       @Query("prop") property: String,
                       @Query("redirects") redirects: String,
                       @Query("format") format: String,
                       @Query("continues") continues: String,
                       @Query("titles") title: String): Observable<Word>

    @GET("w/api.php")
    fun getWordContent(@QueryMap parameters: Map<String, String>): Observable<Word>

    @GET("w/api.php")
    fun getWordImage(@Query("action") action: String,
                     @Query("prop") property: String,
                     @Query("piprop") piProperty: String,
                     @Query("redirects") redirects: String,
                     @Query("format") format: String,
                     @Query("continues") continues: String,
                     @Query("titles") title: String): Observable<URL>

    @GET("w/api.php")
    fun getWordImage(@QueryMap parameters: Map<String, String>): Observable<URL>
}