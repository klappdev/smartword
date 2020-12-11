package org.kl.smartword.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.view.fragment.DictionaryFragment
import org.kl.smartword.view.fragment.CategoryFragment

class SectionPagerAdapter(private val size: Int, manager: FragmentManager) :
      FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (TabOrder.findBy(position)) {
            CATEGORY_TAB -> CategoryFragment()
            DICTIONARY_TAB -> DictionaryFragment()
        }
    }

    fun getItem(tabOrder: TabOrder): Fragment {
        return when (tabOrder) {
            CATEGORY_TAB -> CategoryFragment()
            DICTIONARY_TAB -> DictionaryFragment()
        }
    }

    override fun getCount() = size
}