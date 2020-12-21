package org.kl.smartword.event.word

import org.kl.smartword.view.WordsActivity
import org.kl.smartword.view.adapter.WordsAdapter

class ResetWordListener(
    private val activity: WordsActivity,
    private val wordsAdapter: WordsAdapter
) {
    operator fun invoke(): Boolean {
        wordsAdapter.position = -1
        wordsAdapter.notifyDataSetChanged()

        activity.notifyMenuItemSelected(false)

        return true
    }
}