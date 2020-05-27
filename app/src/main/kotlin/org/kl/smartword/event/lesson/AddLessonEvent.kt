package org.kl.smartword.event.lesson

import android.view.View
import android.widget.Toast
import org.kl.smartword.R
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.AddLessonActivity
import org.kl.smartword.util.formatted
import java.util.*

class AddLessonEvent(private val activity: AddLessonActivity) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val descriptionField = activity.descriptionTextView

    override fun onClick(v: View?) {
        val lessonDB = LessonDB.getInstance(activity.applicationContext)

        if (!validateNameField(lessonDB) || !validateDescriptionField()) {
            return
        }

        val lesson = Lesson().apply {
            icon = R.drawable.lesson_background_1
            name = nameField.text.toString()
            description = descriptionField.text.toString()
            date = Date().formatted()
        }

        LessonDB.getInstance(activity.applicationContext).add(lesson)
        Toast.makeText(activity, "Add lesson: ${lesson.name}", Toast.LENGTH_LONG)
            .show()
    }

    private fun validateNameField(lessonDB: LessonDB) : Boolean {
        val nameLesson: String = nameField.text.toString()

        if (nameLesson.isEmpty()) {
            nameField.requestFocus()
            nameField.error = "Name is empty"
            return false
        }

        if (lessonDB.checkIfExists(nameLesson)) {
            nameField.requestFocus()
            nameField.error = "Lesson already exists"
            return false
        }

        return true
    }

    private fun validateDescriptionField() : Boolean {
        val descriptionLesson: String = descriptionField.text.toString()

        if (descriptionLesson.isEmpty()) {
            descriptionField.requestFocus()
            descriptionField.error = "Description is empty"
            return false
        }

        return true
    }
}