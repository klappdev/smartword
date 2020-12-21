package org.kl.smartword.event.validate

import android.widget.TextView

class ViewValidator {
    private constructor() {
        throw IllegalAccessException("Can't create validator instance")
    }

    companion object {
        @JvmStatic
        fun error(inputField: TextView, message: String) {
            inputField.requestFocus()
            inputField.error = message
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

