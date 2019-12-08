package org.kl.smartword.bean

data class Lesson(var id: Int,
                  var icon: Int,
                  var name: String,
                  var description: String,
                  var date: String,
                  var selected: Boolean)