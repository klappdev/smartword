package org.kl.smartword.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    private constructor() {
        throw IllegalAccessException("Can't create object class")
    }

    companion object {
        fun currentDateTime(): String {
            val dateFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
            return dateFormat.format(Date())
        }

        fun currentDateTime(pattern: String): String {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return dateFormat.format(Date())
        }

        fun currentDateTime(pattern: String, locale: Locale): String {
            val dateFormat = SimpleDateFormat(pattern, locale)
            return dateFormat.format(Date())
        }
    }
}