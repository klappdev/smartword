package org.kl.smartword.event.tab

import android.content.Context
import android.util.Log
import androidx.viewpager.widget.ViewPager
import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*

import org.kl.smartword.ui.adapter.SectionPagerAdapter
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.TopicsFragment

class ChangeTabListener(private val pageAdapter: SectionPagerAdapter,
                        private val context: Context) : ViewPager.OnPageChangeListener {

    override fun onPageSelected(position: Int) {
        Log.i("TAG-CTE", "current tab selected: $position")

        when (TabOrder.findBy(position)) {
            MAIN_TAB -> {
                val fragment = pageAdapter.getItem(position) as TopicsFragment

                /*fragment.updateTopicContents(context, adapter)*/
            }
            DICT_TAB -> {
                val fragment = pageAdapter.getItem(position) as DictionaryFragment

                /*fragment.updateDictionaryContents(context, adapter)*/
            }
        }
    }

    override fun onPageScrollStateChanged(position: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
}