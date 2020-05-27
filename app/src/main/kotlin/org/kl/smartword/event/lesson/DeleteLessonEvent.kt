package org.kl.smartword.event.lesson

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast

import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.adapter.DictionaryAdapter

class DeleteLessonEvent(private val adapter: DictionaryAdapter, private val lesson: Lesson) : View.OnClickListener {
    private lateinit var context: Context

    override fun onClick(view: View?) {
        this.context = view?.context!!

        val dialog = AlertDialog.Builder(view.context)
        dialog.setTitle("Delete lesson")
              .setMessage("Do you want delete lesson?")
              .setCancelable(false)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setPositiveButton("Yes", ::clickPositiveButton)
              .setNegativeButton("No", ::clickNegativeButton)
        dialog.show()
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        val lessonDB = LessonDB.getInstance(context)
        lessonDB.delete(lesson.id)

        adapter.listLessons = lessonDB.getAll()
        adapter.notifyDataSetChanged()

        Toast.makeText(context, "Delete lesson: ${lesson.name}", Toast.LENGTH_LONG)
            .show()
    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }
}