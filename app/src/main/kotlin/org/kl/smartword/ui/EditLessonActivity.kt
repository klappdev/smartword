package org.kl.smartword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.event.lesson.EditLessonListener
import org.kl.smartword.state.LessonState

class EditLessonActivity : AppCompatActivity() {
    internal lateinit var nameTextView: TextView
        private set
    internal lateinit var descriptionTextView: TextView
        private set
    private lateinit var editButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lesson)

        this.nameTextView = findViewById(R.id.name_lesson_text_view)
        this.descriptionTextView = findViewById(R.id.description_lesson_text_view)
        this.editButton = findViewById(R.id.edit_lesson_button)

        val idLesson = intent.getIntExtra("id_lesson", -1)

        val lessonDB = LessonDB.getInstance(applicationContext)
        val lesson = lessonDB.get(idLesson)

        nameTextView.text = lesson.name
        descriptionTextView.text = lesson.description

        editButton.setOnClickListener(EditLessonListener(this, lesson))
    }
}
