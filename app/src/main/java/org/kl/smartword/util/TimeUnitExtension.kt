package org.kl.smartword.util

import java.util.concurrent.TimeUnit

val Int.ns: Long get() = TimeUnit.NANOSECONDS.toNanos(this.toLong())
val Int.ms: Long get() = TimeUnit.MILLISECONDS.toMillis(this.toLong())
val Int.us: Long get() = TimeUnit.MICROSECONDS.toMicros(this.toLong())
val Int.s: Long get() = TimeUnit.SECONDS.toSeconds(this.toLong())
val Int.min: Long get() = TimeUnit.MINUTES.toMinutes(this.toLong())
val Int.h: Long get() = TimeUnit.HOURS.toHours(this.toLong())
val Int.d: Long get() = TimeUnit.DAYS.toDays(this.toLong())

val Long.ns: Long get() = TimeUnit.NANOSECONDS.toNanos(this)
val Long.ms: Long get() = TimeUnit.MILLISECONDS.toMillis(this)
val Long.us: Long get() = TimeUnit.MICROSECONDS.toMicros(this)
val Long.s: Long get() = TimeUnit.SECONDS.toSeconds(this)
val Long.min: Long get() = TimeUnit.MINUTES.toMinutes(this)
val Long.h: Long get() = TimeUnit.HOURS.toHours(this)
val Long.d: Long get() = TimeUnit.DAYS.toDays(this)