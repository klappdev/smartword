package org.kl.smartword.event.validate

import android.widget.TextView

class ViewValidator {
    private constructor() {
        throw IllegalAccessException("Can't create validator instance")
    }

    companion object {
        @JvmStatic
        fun validate(inputField: TextView, message: String, predicate: (String) -> Boolean) : Boolean {
            val text: String = inputField.text.toString()

            if (predicate(text)) {
                inputField.requestFocus()
                inputField.error = message
                return false
            }

            return true
        }

        @JvmStatic
        fun validate(inputField: TextView, message: String) : Boolean {
            val text: String = inputField.text.toString()

            if (text.isEmpty()) {
                inputField.requestFocus()
                inputField.error = message
                return false
            }

            return true
        }
    }
}