package org.kl.smartword.model

import org.kl.smartword.util.formatted
import java.util.*

data class Word(val id: Long,
                val idLesson: Long,
                val name: String,
                val transcription: String?,
                val translation: String,
                val association: String?,
                val etymology: String?,
                val otherForm: String,
                val antonym: String?,
                val irregular: String?,
                val date: String = Date().formatted()) {
    constructor() : this(0, 0, "", "[]", "", "", "", "", "", "")
}