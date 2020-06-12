package org.kl.smartword.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.kl.smartword.model.Word

object WordDB {
    private const val TAG = "TAG-WDB"
    internal var database: SQLiteDatabase? = null

	fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS word (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
							 id_lesson INTEGER NOT NULL, 
                             icon INTEGER NOT NULL, 
                             name TEXT NOT NULL,
                             transcription TEXT NOT NULL,
                             translation TEXT NOT NULL,							 
							 date TEXT NOT NULL,
							 association TEXT NOT NULL,
							 etymology TEXT NOT NULL,
							 other_form TEXT NOT NULL,
							 antonym TEXT NOT NULL,
							 irregular TEXT NOT NULL,
                             selected INTEGER NOT NULL DEFAULT 0 CHECK(selected IN (0,1)));
                          """)

        Log.d(TAG, "Create table word")
    }
	
	fun upgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		if (oldVersion != newVersion) {            
            database?.execSQL("DROP TABLE IF EXISTS word")
            create(database)
        }

        Log.d(TAG, "Upgrade table word")
	}

    fun add(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("icon", word.icon)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("date", word.date)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("other_form", word.otherForm)
        values.put("antonym", word.antonym)
        values.put("irregular", word.irregular)
        values.put("selected", word.selected)

        val rowId = database?.insert("word", null, values)

        Log.d(TAG, "Inserted new row table: $rowId")
    }

    fun update(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("icon", word.icon)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("date", word.date)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("other_form", word.otherForm)
        values.put("antonym", word.antonym)
        values.put("irregular", word.irregular)
        values.put("selected", word.selected)

        database?.update("word", values, "id = ?", arrayOf(word.id.toString()))

        Log.d(TAG, "Updated row table: ${word.id}")
    }

    fun delete(id: Int) {
        database?.delete("word", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun checkIfExists(name: String): Boolean {
        var result = false
        val cursor = database?.rawQuery("SELECT * FROM word WHERE name=?", arrayOf(name.trim()))

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = true
            }
        } finally {
            cursor?.close()
        }

        return result
    }

    fun get(id: Int): Word {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("word", null, "id = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Word(cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("id_lesson")),
                            cursor.getInt(cursor.getColumnIndex("icon")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("transcription")),
                            cursor.getString(cursor.getColumnIndex("translation")),
                            cursor.getString(cursor.getColumnIndex("date")),
                            cursor.getString(cursor.getColumnIndex("association")),
                            cursor.getString(cursor.getColumnIndex("etymology")),
                            cursor.getString(cursor.getColumnIndex("other_form")),
                            cursor.getString(cursor.getColumnIndex("antonym")),
                            cursor.getString(cursor.getColumnIndex("irregular")),
                            cursor.getInt(cursor.getColumnIndex("selected")) != 0
                )
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve row table: $id")

        return Word()
    }

    fun getAll(): List<Word> {
        val words = mutableListOf<Word>()
        val cursor = database?.query("word", null, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("id_lesson")),
                        cursor.getInt(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("transcription")),
                        cursor.getString(cursor.getColumnIndex("translation")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("other_form")),
                        cursor.getString(cursor.getColumnIndex("antonym")),
                        cursor.getString(cursor.getColumnIndex("irregular")),
                        cursor.getInt(cursor.getColumnIndex("selected")) != 0
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all rows table")

        return words
    }

    fun getAllByIdLesson(idLesson: Int): List<Word> {
        val words = mutableListOf<Word>()
        val arguments = arrayOf(idLesson.toString())
        val cursor = database?.query("word", null, "id_lesson = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("id_lesson")),
                        cursor.getInt(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("transcription")),
                        cursor.getString(cursor.getColumnIndex("translation")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("other_form")),
                        cursor.getString(cursor.getColumnIndex("antonym")),
                        cursor.getString(cursor.getColumnIndex("irregular")),
                        cursor.getInt(cursor.getColumnIndex("selected")) != 0
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all rows table")

        return words
    }
}