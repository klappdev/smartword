package org.kl.smartword.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatted() : String {
    val dateFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
    return dateFormat.format(this)
}

fun Date.formatted(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.formatted(pattern: String, locale: Locale): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    return dateFormat.format(this)
}
