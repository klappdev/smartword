package org.kl.smartword.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.TopicsFragment

class SectionPagerAdapter(private val size: Int, manager: FragmentManager) :
      FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (TabOrder.findBy(position)) {
            MAIN_TAB -> TopicsFragment()
            DICT_TAB -> DictionaryFragment()
        }
    }

    override fun getCount() = size
}