package org.kl.smartword.bean

data class Word(var id: Int,
                var idLesson: Int,
                var icon: Int,
                var name: String,
                var transcription: String?,
                var translation: String,
                var date: String,
                var association: String?,
                var etymology: String?,
                var otherForm: String,
                var antonym: String?,
                var irregular: String?,
                var selected: Boolean) {
    constructor() : this(0, 0, 0, "", "", "", "", "", "", "", "", "", false)
}