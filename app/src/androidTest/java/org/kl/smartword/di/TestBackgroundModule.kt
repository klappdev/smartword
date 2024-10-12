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
package org.kl.smartword.di

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

import dagger.Module
import dagger.Provides

import javax.inject.Named
import javax.inject.Singleton

import org.kl.smartword.util.seconds
import org.kl.smartword.work.LoadLessonService
import org.kl.smartword.work.LoadWordService
import org.kl.smartword.work.MediateReceiver

@Module
class TestBackgroundModule {

    @Provides
    @Singleton
    @Named("LessonService")
    fun provideLessonComponentName(context: Context) : ComponentName {
        return ComponentName(context, LoadLessonService::class.java)
    }

    @Provides
    @Singleton
    @Named("WordService")
    fun provideWordComponentName(context: Context) : ComponentName {
        return ComponentName(context, LoadWordService::class.java)
    }

    @Provides
    @Singleton
    @Named("LessonService")
    fun provideLessonJobInfo(@Named("LessonService") serviceComponent: ComponentName): JobInfo {
        return JobInfo.Builder(LoadLessonService.JOB_ID, serviceComponent)
            .setMinimumLatency(1.seconds)
            .setOverrideDeadline(10.seconds)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .build()
    }

    @Provides
    @Singleton
    @Named("WordService")
    fun provideWordJobInfo(@Named("WordService") serviceComponent: ComponentName): JobInfo {
        return JobInfo.Builder(LoadWordService.JOB_ID, serviceComponent)
            .setMinimumLatency(1.seconds)
            .setOverrideDeadline(10.seconds)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideJobScheduler(context: Context): JobScheduler {
        return context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }

    @Provides
    fun provideMediateReceiver(): MediateReceiver {
        return MediateReceiver()
    }
}