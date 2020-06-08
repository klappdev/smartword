package org.kl.smartword.event.word

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast

import org.kl.smartword.bean.Word
import org.kl.smartword.db.WordDB
import org.kl.smartword.ui.adapter.WordsAdapter

class DeleteWordListener(private val adapter: WordsAdapter,
                         private val word: Word) : View.OnClickListener {
    private lateinit var context: Context

    override fun onClick(view: View?) {
        this.context = view?.context!!

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Delete word")
              .setMessage("Do you want delete word?")
              .setCancelable(false)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setPositiveButton("Yes", ::clickPositiveButton)
              .setNegativeButton("No",  ::clickNegativeButton)
        dialog.show()
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        WordDB.delete(word.id)

        adapter.listWords = WordDB.getAllByIdLesson(word.idLesson)
        adapter.notifyDataSetChanged()

        Toast.makeText(context, "Delete word: ${word.name}", Toast.LENGTH_LONG)
             .show()
    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }
}