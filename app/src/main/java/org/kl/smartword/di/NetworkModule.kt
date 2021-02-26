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
package org.kl.smartword.di

import android.content.Context
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import org.kl.smartword.net.DictionaryService
import org.kl.smartword.net.NetworkConnectivityHelper

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return DictionaryService.createOkHttpClient()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return DictionaryService.createGson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return DictionaryService.createRetrofit(gson, client)
    }

    @Provides
    @Singleton
    fun provideRemoteService(retrofit: Retrofit): DictionaryService {
        return retrofit.create(DictionaryService::class.java)
    }

    @Provides
    @Singleton
    fun providesNetworkConnectivityHelper(context: Context): NetworkConnectivityHelper {
        return NetworkConnectivityHelper(context)
    }
}