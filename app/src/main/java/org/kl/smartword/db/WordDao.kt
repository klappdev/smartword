package org.kl.smartword.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.Observable

import org.kl.smartword.model.Word

object WordDao {
    private const val TAG = "TAG-WDB"
    internal var database: SQLiteDatabase? = null

	fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS word (
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
	
	fun upgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		if (oldVersion != newVersion) {            
            database?.execSQL("DROP TABLE IF EXISTS word")
            create(database)
        }

        Log.d(TAG, "Upgrade table word")
	}

    private fun addSynchronously(word: Word) {
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

    fun add(word: Word): Completable {
        return Completable.fromRunnable {
            addSynchronously(word)
        }
    }

    private fun updateSynchronously(word: Word) {
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

    fun update(word: Word): Completable {
        return Completable.fromRunnable {
            updateSynchronously(word)
        }
    }

    private fun deleteSynchronously(id: Long) {
        database?.delete("word", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun delete(id: Long): Completable {
        return Completable.fromRunnable {
            deleteSynchronously(id)
        }
    }

    private fun checkIfExistsSynchronously(name: String): Boolean {
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

    fun checkIfExists(name: String): Single<Boolean> {
        return Single.fromCallable {
            checkIfExistsSynchronously(name)
        }
    }

    private fun getByIdSynchronously(id: Long): Word {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("word", null, "id = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Word(cursor.getLong(cursor.getColumnIndex("id")),
                            cursor.getLong(cursor.getColumnIndex("id_lesson")),
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

        Log.d(TAG, "Retrieve row table by id: $id")

        return Word()
    }

    fun getById(id: Long): Maybe<Word> {
        return Maybe.fromCallable {
            getByIdSynchronously(id)
        }
    }

    private fun getAllSynchronously(): List<Word> {
        val words = mutableListOf<Word>()
        val cursor = database?.query("word", null, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
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

    fun getAll(): Observable<List<Word>> {
        return Observable.fromCallable(::getAllSynchronously)
    }

    private fun getAllByIdLessonSynchronously(idLesson: Long): List<Word> {
        val words = mutableListOf<Word>()
        val arguments = arrayOf(idLesson.toString())
        val cursor = database?.query("word", null, "id_lesson = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
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

        Log.d(TAG, "Search all rows table by id lesson")

        return words
    }

    fun getAllByIdLesson(idLesson: Long): Observable<List<Word>> {
        return Observable.fromCallable {
            getAllByIdLessonSynchronously(idLesson)
        }
    }

    private fun searchByNameSynchronously(name: String): List<Word> {
        val words = mutableListOf<Word>()
        val arguments = arrayOf("%${name}%")
        val cursor = database?.query("word", null, "name LIKE ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
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

        Log.d(TAG, "Search all rows table by name: $name")

        return words
    }

    fun searchByName(name: String): Observable<List<Word>> {
        return Observable.fromCallable {
            searchByNameSynchronously(name)
        }
    }

    private fun sortByNameSynchronously(asc: Boolean): List<Word> {
        val words = mutableListOf<Word>()
        val orderBy = if (asc) "name ASC" else "name DESC"
        val cursor = database?.query("word", null, null, null, null, null, orderBy)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
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

        Log.d(TAG, "Sort all rows table by $orderBy")

        return words
    }

    fun sortByName(asc: Boolean): Observable<List<Word>> {
        return Observable.fromCallable {
            sortByNameSynchronously(asc)
        }
    }
}