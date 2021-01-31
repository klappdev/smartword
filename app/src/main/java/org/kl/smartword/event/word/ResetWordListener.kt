package org.kl.smartword.event.word

import org.kl.smartword.view.ShowLessonActivity
import org.kl.smartword.view.adapter.LessonAdapter

class ResetWordListener(
    private val activity: ShowLessonActivity,
    private val wordsAdapter: LessonAdapter
) {
    operator fun invoke(): Boolean {
        wordsAdapter.position = -1
        wordsAdapter.notifyDataSetChanged()

        activity.notifyMenuItemSelected(false)

        return true
    }
}