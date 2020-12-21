package org.kl.smartword.model

import org.kl.smartword.util.formatted
import java.util.*

data class Lesson(val id: Long,
                  val name: String,
                  val description: String,
                  val date: String = Date().formatted(),
                  val iconUrl: String = "") {
    constructor() : this(0, "", "")
}