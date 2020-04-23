package org.kl.smartword.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.kl.smartword.bean.Word

class WordDB : SQLiteOpenHelper {
	companion object {
        private const val TAG = "TAG-WDB"

		@Volatile @JvmStatic
        private var instance: WordDB? = null

        @JvmStatic
		private var database: SQLiteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): WordDB {
            if (instance == null) {
                synchronized(WordDB::class) {
                    if (instance == null) {
                        instance = WordDB(context.applicationContext)
                        database = instance?.writableDatabase
                    }
                }
			}

            return instance!!
        }
    }   
	
	private constructor(context: Context?) : super(context, "smartword.db", null, 1)
	
	override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE word (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
							 id_lesson INTEGER NOT NULL, 
                             name TEXT NOT NULL,
                             transcription TEXT NOT NULL,
                             translation TEXT NOT NULL,							 
							 date TEXT NOT NULL,
							 association TEXT NOT NULL,
							 etymology TEXT NOT NULL,
							 other_form TEXT NOT NULL,
							 antonym TEXT NOT NULL,
							 irregular TEXT NOT NULL);
                          """)

        Log.d(TAG, "Create table word")
    }
	
	override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		if (oldVersion != newVersion) {            
            database?.execSQL("DROP TABLE IF EXISTS word")
            onCreate(database)
        }

        Log.d(TAG, "Upgrade table word")
	}

    fun add(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("date", word.date)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("other_form", word.otherForm)
        values.put("antonym", word.antonym)
        values.put("irregular", word.irregular)

        val rowId = database?.insert("word", null, values)

        Log.d(TAG, "Inserted new row table: $rowId")
    }

    fun update(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("date", word.date)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("other_form", word.otherForm)
        values.put("antonym", word.antonym)
        values.put("irregular", word.irregular)

        database?.update("word", values, "id = ?", arrayOf(word.id.toString()))

        Log.d(TAG, "Updated row table: ${word.id}")
    }

    fun delete(id: Int) {
        database?.delete("word", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun get(id: Int): Word {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("word", null, "id = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Word(cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("id_lesson")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("transcription")),
                            cursor.getString(cursor.getColumnIndex("translation")),
                            cursor.getString(cursor.getColumnIndex("date")),
                            cursor.getString(cursor.getColumnIndex("association")),
                            cursor.getString(cursor.getColumnIndex("etymology")),
                            cursor.getString(cursor.getColumnIndex("other_form")),
                            cursor.getString(cursor.getColumnIndex("antonym")),
                            cursor.getString(cursor.getColumnIndex("irregular"))
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
        val cursor = database?.query("lesson", null, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("id_lesson")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("transcription")),
                        cursor.getString(cursor.getColumnIndex("translation")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("other_form")),
                        cursor.getString(cursor.getColumnIndex("antonym")),
                        cursor.getString(cursor.getColumnIndex("irregular"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all rows table")

        return words
    }

    override fun close() {
        if (instance != null) {
            database?.close()
        }
    }
}