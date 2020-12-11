package org.kl.smartword.event.lesson

import org.kl.smartword.view.MainActivity
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.fragment.DictionaryFragment

class ResetLessonListener {
    private val activity: MainActivity
    private val dictionaryAdapter: DictionaryAdapter

    constructor(dictionaryFragment: DictionaryFragment) {
        this.activity = dictionaryFragment.activity as MainActivity
        this.dictionaryAdapter = dictionaryFragment.dictionaryAdapter
    }

    operator fun invoke(): Boolean {
        dictionaryAdapter.position = -1
        dictionaryAdapter.notifyDataSetChanged()

        activity.notifyMenuItemSelected(false)

        return true
    }
}