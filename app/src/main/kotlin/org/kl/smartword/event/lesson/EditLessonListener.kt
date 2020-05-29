package org.kl.smartword.event.lesson

import android.view.View
import android.widget.Toast
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.ui.EditLessonActivity

class EditLessonListener(private val activity: EditLessonActivity,
                         private val lesson: Lesson) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val descriptionField = activity.descriptionTextView

    override fun onClick(view: View?) {
        val lessonDB = LessonDB.getInstance(activity.applicationContext)

        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(descriptionField, "Description is empty")) {
            return
        }

        lesson.name = nameField.text.toString()
        lesson.description = descriptionField.text.toString()

        lessonDB.update(lesson)
        Toast.makeText(activity, "Update lesson: ${lesson.name}", Toast.LENGTH_LONG)
             .show()
    }
}