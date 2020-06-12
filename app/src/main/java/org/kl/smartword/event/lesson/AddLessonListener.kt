package org.kl.smartword.event.lesson

import android.view.View
import android.widget.Toast
import org.kl.smartword.R
import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.ui.AddLessonActivity
import org.kl.smartword.util.formatted
import java.util.*

class AddLessonListener(private val activity: AddLessonActivity) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val descriptionField = activity.descriptionTextView

    override fun onClick(view: View?) {
        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(nameField, "Lesson already exists", LessonDB::checkIfExists) ||
            !ViewValidator.validate(descriptionField, "Description is empty")) {
            return
        }

        val lesson = Lesson().apply {
            icon = R.drawable.theme_icon
            name = nameField.text.toString()
            description = descriptionField.text.toString()
            date = Date().formatted()
        }

        LessonDB.add(lesson)
        Toast.makeText(activity, "Add lesson: ${lesson.name}", Toast.LENGTH_LONG)
             .show()
    }
}
