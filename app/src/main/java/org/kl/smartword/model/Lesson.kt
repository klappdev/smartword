package org.kl.smartword.model

import java.util.Date
import org.kl.smartword.util.formatted

data class Lesson(val id: Long,
                  val name: String,
                  val description: String,
                  val date: String = Date().formatted(),
                  val iconUrl: String = "") {
    constructor() : this(0, "", "")
}