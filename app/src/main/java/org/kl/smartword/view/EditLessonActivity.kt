package org.kl.smartword.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.lesson.EditLessonListener
import org.kl.smartword.model.Lesson

class EditLessonActivity : AppCompatActivity() {
    private lateinit var editButton: Button
    lateinit var nameTextView: TextView
        private set
    lateinit var descriptionTextView: TextView
        private set
    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lesson)

        this.nameTextView = findViewById(R.id.name_lesson_text_view)
        this.descriptionTextView = findViewById(R.id.description_lesson_text_view)
        this.editButton = findViewById(R.id.edit_lesson_button)

        val idLesson = intent.getLongExtra("id_lesson", -1)

        disposables.add(LessonDao.getById(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableMaybeObserver<Lesson>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSuccess(result: Lesson) {
                    nameTextView.text = result.name
                    descriptionTextView.text = result.description
                }
            }))

        editButton.setOnClickListener(EditLessonListener(this, idLesson))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
