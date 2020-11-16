package org.kl.smartword.model

data class Lesson(val id: Long,
                  val name: String,
                  val description: String,
                  val date: String,
                  val iconUrl: String = "") {
    constructor() : this(0, "", "", "", "")
}