package org.kl.smartword.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.MainFragment

class SectionPagerAdapter(private val size: Int, manager: FragmentManager) :
      FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (TabOrder.valueOf(position)) {
            MAIN_TAB -> {
                Log.i("TAG-SPA", "Tab main was selected!")

                MainFragment()
            }
            DICT_TAB -> {
                Log.i("TAG-SPA", "Tab dictionary was selected!")

                DictionaryFragment()
            }
            else -> throw IllegalAccessException("Unknown tab order")
        }
    }

    override fun getCount() = size
}