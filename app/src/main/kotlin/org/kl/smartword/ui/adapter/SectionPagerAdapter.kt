package org.kl.smartword.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.TopicFragment

class SectionPagerAdapter(private val size: Int, manager: FragmentManager) :
      FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (TabOrder.findBy(position)) {
            MAIN_TAB -> TopicFragment()
            DICT_TAB -> DictionaryFragment()
        }
    }

    override fun getCount() = size
}