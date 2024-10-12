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
package org.kl.smartword.util

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.internal.schedulers.TrampolineScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {

    override fun apply(base: Statement, description: Description) = object: Statement() {
        override fun evaluate() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler{ TrampolineScheduler.instance() }
            RxJavaPlugins.setIoSchedulerHandler{ TrampolineScheduler.instance() }
            RxJavaPlugins.setNewThreadSchedulerHandler{ TrampolineScheduler.instance() }
            RxJavaPlugins.setComputationSchedulerHandler{ TrampolineScheduler.instance() }

            try {
                base.evaluate()
            } finally {
                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}