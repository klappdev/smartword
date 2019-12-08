package org.kl.smartword.state

enum class TabOrder(val position: Int) {
    MAIN_TAB(0),
    DICT_TAB(1);

    companion object {
        private val cache = HashMap<Int, TabOrder>()

        init {
            for (item in values()) {
                cache[item.position] = item
            }
        }

        fun valueOf(position: Int) = cache[position]
    }
}