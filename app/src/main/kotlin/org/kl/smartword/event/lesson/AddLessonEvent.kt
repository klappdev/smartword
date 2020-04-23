package org.kl.smartword.event.lesson

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.AddLessonActivity
import org.kl.smartword.util.formatted
import java.util.*

class AddLessonEvent(private val activity: AddLessonActivity) : View.OnClickListener {

    override fun onClick(v: View?) {
        val nameLesson: String = activity.nameTextView.text.toString()
        val descriptionLesson: String = activity.descriptionTextView.text.toString()

        if (nameLesson.isEmpty()) {
            Toast.makeText(activity, "Name is empty", Toast.LENGTH_LONG).show()
            return
        }

        if (descriptionLesson.isEmpty()) {
            Toast.makeText(activity, "Description is empty", Toast.LENGTH_LONG).show()
            return
        }

        val lesson = Lesson().apply {
            icon = R.drawable.lesson_background_1
            name = nameLesson
            description = descriptionLesson
            date = Date().formatted()
        }

        LessonDB.getInstance(activity.applicationContext).add(lesson)
        Toast.makeText(activity, "Add lesson: $lesson", Toast.LENGTH_LONG).show()
    }
}