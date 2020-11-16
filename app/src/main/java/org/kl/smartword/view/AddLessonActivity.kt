package org.kl.smartword.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

import org.kl.smartword.R
import org.kl.smartword.event.lesson.AddLessonListener

class AddLessonActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    lateinit var nameTextView: TextView
        private set
    lateinit var descriptionTextView: TextView
        private set
    var disposables: CompositeDisposable = CompositeDisposable()
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lesson)

        this.nameTextView = findViewById(R.id.name_lesson_text_view)
        this.descriptionTextView = findViewById(R.id.description_lesson_text_view)
        this.addButton = findViewById(R.id.add_lesson_button)

        addButton.setOnClickListener(AddLessonListener(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

