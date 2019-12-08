package org.kl.smartword.bean

data class Word(val id: Int,
                val idLesson: Int,
                val name: String,
                val transcription: String?,
                val translation: String,
                val date: String,
                val association: String?,
                val etymology: String?,
                val otherForm: String,
                val antonym: String?,
                val irregular: String?)