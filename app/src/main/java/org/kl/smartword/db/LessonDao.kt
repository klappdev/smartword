package org.kl.smartword.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.util.Log

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.Completable
import io.reactivex.Observable

import org.kl.smartword.model.Lesson

object LessonDao {
    private const val TAG = "TAG-LDB"
    internal var database: SQLiteDatabase? = null

    fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS lesson (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             name TEXT NOT NULL,
                             description TEXT NOT NULL,
                             date TEXT NOT NULL,
                             icon_url TEXT NOT NULL);
                          """
        )

        Log.d(TAG, "Create table lesson")
    }

    fun upgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            database?.execSQL("DROP TABLE IF EXISTS lesson")
            create(database)
        }

        Log.d(TAG, "Upgrade table lesson")
    }

    private fun addSynchronously(lesson: Lesson) {
        val values = ContentValues()
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("icon_url", lesson.iconUrl)

        val rowId: Long? = database?.insert("lesson", null, values)

        Log.d(TAG, "Inserted new row table: $rowId")
    }

    fun add(lesson: Lesson): Completable {
        return Completable.fromRunnable {
            addSynchronously(lesson)
        }
    }

    private fun addAllSynchronously(lessons: List<Lesson>) {
        lessons.forEach(::addSynchronously)
    }

    fun addAll(lessons: List<Lesson>): Completable {
        return Completable.fromRunnable {
            addAllSynchronously(lessons)
        }
    }

    private fun updateSynchronously(lesson: Lesson) {
        val values = ContentValues()
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("icon_url", lesson.iconUrl)

        database?.update("lesson", values, "id = ?", arrayOf(lesson.id.toString()))

        Log.d(TAG, "Updated row table: ${lesson.id}")
    }

    fun update(lesson: Lesson): Completable {
        return Completable.fromRunnable {
            updateSynchronously(lesson)
        }
    }

    private fun updateAllSynchronously(vararg lessons: Lesson) {
        lessons.forEach(::updateSynchronously)
    }

    fun updateAll(vararg lessons: Lesson): Completable {
        return Completable.fromRunnable {
            updateAllSynchronously(*lessons)
        }
    }

    private fun deleteSynchronously(id: Long) {
        database?.delete("lesson", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun delete(id: Long): Completable {
        return Completable.fromRunnable {
            deleteSynchronously(id)
        }
    }

    private fun checkIfExistsSynchronously(name: String): Boolean {
        var result = false
        val cursor = database?.rawQuery("SELECT * FROM lesson WHERE name=?", arrayOf(name.trim()))

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

    private fun getByIdSynchronously(id: Long): Lesson {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("lesson", null, "id = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Lesson(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("icon_url"))
                )
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all row table by id: $id")

        return Lesson()
    }

    fun getById(id: Long): Maybe<Lesson> {
        return Maybe.fromCallable {
            getByIdSynchronously(id)
        }
    }

    private fun getAllSynchronously(): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val cursor: Cursor? = database?.query("lesson", null, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lessons += Lesson(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("icon_url"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all rows table")

        return lessons
    }

    fun getAll(): Observable<List<Lesson>> {
        return Observable.fromCallable(::getAllSynchronously)
    }

    private fun searchByNameSynchronously(name: String): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val arguments = arrayOf("%${name}%")
        val cursor = database?.query("lesson", null, "name LIKE ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lessons += Lesson(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("icon_url"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Search all rows table by name: $name")

        return lessons
    }

    fun searchByName(name: String): Observable<List<Lesson>> {
        return Observable.fromCallable {
            searchByNameSynchronously(name)
        }
    }

    private fun sortByNameSynchronously(asc: Boolean): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val columns = arrayOf("id", "name", "description", "date", "icon_url")
        val orderBy = if (asc) "name ASC" else "name DESC"
        val cursor = database?.query("lesson", columns, null, null, null, null, orderBy)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lessons += Lesson(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("icon_url"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Sort all rows table by $orderBy")

        return lessons
    }

    fun sortByName(asc: Boolean): Observable<List<Lesson>> {
        return Observable.fromCallable {
            sortByNameSynchronously(asc)
        }
    }
}