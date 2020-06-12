package org.kl.smartword.state

enum class LessonState(val position: Int) {
    NONE(-1),
    ADDED(0),
    UPDATED(1),
    DELETED(2);

    companion object {
        @JvmStatic
        private val cache: Map<Int, LessonState> = values().associateBy(LessonState::position)

        @JvmStatic
        fun findBy(position: Int) : LessonState {
            return cache[position] ?: error("Unknown lesson state")
        }
    }
}