package org.kl.smartword.event.tab

import android.util.Log
import androidx.viewpager.widget.ViewPager
import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.ui.adapter.SectionPagerAdapter
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.MainFragment

class ChangeTabEvent(private val pageAdapter: SectionPagerAdapter) : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(position: Int) {
        Log.i("TAG-CTE", "current tab selected: $position")

        when (TabOrder.valueOf(position)) {
            MAIN_TAB -> {
                val fragment = pageAdapter.getItem(position) as MainFragment
                fragment.hidden = false
            }
            DICT_TAB -> {
                val fragment = pageAdapter.getItem(position) as DictionaryFragment
                fragment.hidden = false
            }
            else -> throw IllegalAccessException("Unknown tab order")
        }

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {}
}