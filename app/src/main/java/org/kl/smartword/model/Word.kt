package org.kl.smartword.model

data class Word(val id: Long,
                val idLesson: Long,
                val name: String,
                val transcription: String?,
                val translation: String,
                val date: String,
                val association: String?,
                val etymology: String?,
                val otherForm: String,
                val antonym: String?,
                val irregular: String?) {
    constructor() : this(0, 0, "", "[]", "", "", "", "", "", "", "")
}