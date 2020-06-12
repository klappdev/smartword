package org.kl.smartword.state

enum class TabOrder(val position: Int) {
    MAIN_TAB(0),
    DICT_TAB(1);
    /*SETTING_TAB*/

    companion object {
        @JvmStatic
        private val cache: Map<Int, TabOrder> = values().associateBy(TabOrder::position)

        @JvmStatic
        fun findBy(position: Int) : TabOrder {
            return cache[position] ?: error("Unknown tab order")
        }
    }
}