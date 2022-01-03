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

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import org.kl.smartword.R

class CommonSettings private constructor(context: Context) {
    public val preferences: SharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
    public val availableThemes: Array<String> = context.resources.getStringArray(R.array.theme_names)
    public val availableLanguages: Array<String> = context.resources.getStringArray(R.array.language_full_names)
    private val availableCodeLanguages: Array<String> = context.resources.getStringArray(R.array.language_short_names)
    public val availablePartsOfSpeech: Array<String> = context.resources.getStringArray(R.array.parts_of_speech)

    init {
        DEFAULT_LANGUAGE = availableCodeLanguages[0]
        DEFAULT_THEME = availableThemes[0]
    }

    companion object {
        private const val SETTINGS_NAME = "dict_settings"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_THEME = "theme"

        @JvmStatic
        private lateinit var DEFAULT_LANGUAGE: String
        @JvmStatic
        private lateinit var DEFAULT_THEME: String
        @Volatile @JvmStatic
        private var instance: CommonSettings? = null

        @JvmStatic
        fun getInstance(context: Context): CommonSettings {
            if (instance == null) {
                synchronized(CommonSettings::class) {
                    if (instance == null) {
                        instance = CommonSettings(context)
                    }
                }
            }

            return instance!!
        }
    }

    val isDefaultLanguage: Boolean
        get() = readLanguageSynchronously() == DEFAULT_LANGUAGE
    val isDefaultTheme: Boolean
        get() = readThemeSynchronously() == DEFAULT_THEME

    private fun writeLanguageSynchronously(language: String) {
        preferences.edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    fun writeLanguage(language: String): Completable {
        return Completable.fromRunnable { writeLanguageSynchronously(language) }
    }

    fun readLanguageSynchronously(): String {
        if (KEY_LANGUAGE in preferences) {
            return preferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: ""
        }

        return ""
    }

    fun readLanguage(): Observable<String> {
        return Observable.fromCallable(::readLanguageSynchronously)
    }

    private fun writeThemeSynchronously(theme: String) {
        preferences.edit()
            .putString(KEY_THEME, theme)
            .apply()
    }

    fun writeTheme(theme: String): Completable {
        return Completable.fromRunnable { writeThemeSynchronously(theme) }
    }

    private fun readThemeSynchronously(): String {
        if (KEY_THEME in preferences) {
            return preferences.getString(KEY_THEME, DEFAULT_THEME) ?: ""
        }

        return ""
    }

    fun readTheme(): Observable<String> {
        return Observable.fromCallable(::readThemeSynchronously)
    }
}