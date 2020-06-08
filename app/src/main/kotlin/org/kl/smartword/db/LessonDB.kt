package org.kl.smartword.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.kl.smartword.bean.Lesson
import org.kl.smartword.state.LessonState

object LessonDB {
    private const val TAG = "TAG-LDB"
    internal var database: SQLiteDatabase? = null

    fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS lesson (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             icon INTEGER NOT NULL, 
                             name TEXT NOT NULL,
                             description TEXT NOT NULL,
                             date TEXT NOT NULL,
                             selected INTEGER NOT NULL DEFAULT 0 CHECK(selected IN (0,1)));
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

    fun drop() {
        database?.execSQL("DROP TABLE IF EXISTS lesson")

        Log.d(TAG, "Drop table lesson")
    }

    fun add(lesson: Lesson): Long? {
        val values = ContentValues()
        values.put("icon", lesson.icon)
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("selected", lesson.selected)

        val rowId: Long? = database?.insert("lesson", null, values)

        Log.d(TAG, "Inserted new row table: $rowId")

        return rowId
    }

    fun addAll(vararg lessons: Lesson) {
        lessons.forEach { add(it) }
    }

    fun update(lesson: Lesson) {
        val values = ContentValues()
        values.put("icon", lesson.icon)
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("selected", lesson.selected)

        database?.update("lesson", values, "id = ?", arrayOf(lesson.id.toString()))

        Log.d(TAG, "Updated row table: ${lesson.id}")
    }

    fun updateAll(vararg lessons: Lesson) {
        lessons.forEach { update(it) }
    }

    fun delete(id: Int) {
        database?.delete("lesson", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun countRows(): Int {
        var result = 0
        val cursor = database?.rawQuery("SELECT COUNT(*) FROM lesson", null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getInt(0)
            }
        } finally {
            cursor?.close()
        }

        return result
    }

    fun checkIfExists(name: String): Boolean {
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

    fun get(id: Int): Lesson {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("lesson", null, "id = ?", arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return Lesson(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("icon")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getInt(cursor.getColumnIndex("selected")) != 0
                )
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all row table: $id")

        return Lesson()
    }

    fun getAll(): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val cursor: Cursor? = database?.query("lesson", null, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lessons += Lesson(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getInt(cursor.getColumnIndex("selected")) != 0
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Log.d(TAG, "Retrieve all rows table")

        return lessons
    }
}