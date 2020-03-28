package org.kl.smartword.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import org.kl.smartword.state.TabOrder
import org.kl.smartword.state.TabOrder.*
import org.kl.smartword.ui.tab.DictionaryFragment
import org.kl.smartword.ui.tab.TopicFragment

private const val TAG = "TAG-SPA"

class SectionPagerAdapter(private val size: Int, manager: FragmentManager) :
      FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        Log.i(TAG, "Tab click: $position")

        return when (TabOrder.findBy(position)) {
            MAIN_TAB -> {
                Log.i(TAG, "Tab main was selected!")

                TopicFragment()
            }
            DICT_TAB -> {
                Log.i(TAG, "Tab dictionary was selected!")

                DictionaryFragment()
            }
        }
    }

    override fun getCount() = size
}