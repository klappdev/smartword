package org.kl.smartword.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.kl.smartword.bean.Lesson

class LessonDB : SQLiteOpenHelper {
	companion object {
        private const val TAG = "TAG-LDB"

		@Volatile @JvmStatic
        private var instance: LessonDB? = null

        @JvmStatic
		private var database: SQLiteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): LessonDB {
            if (instance == null) {
                synchronized(LessonDB::class) {
                    if (instance == null) {
                        instance = LessonDB(context.applicationContext)
                        database = instance?.writableDatabase
                    }
                }
			}

            return instance!!
        }
    }   

    private constructor(context: Context?) : super(context, "smartword.db", null, 1)

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE lesson (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             icon INTEGER NOT NULL, 
                             name TEXT NOT NULL,
                             description TEXT NOT NULL,
                             date TEXT NOT NULL,
                             selected INTEGER NOT NULL DEFAULT 0 CHECK(selected IN (0,1)));
                          """)

        Log.d(TAG, "Create table lesson")
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		if (oldVersion != newVersion) {
            database?.execSQL("DROP TABLE IF EXISTS lesson")
            onCreate(database)
        }

        Log.d(TAG, "Upgrade table lesson")
	}

    fun drop() {
        database?.execSQL("DROP TABLE IF EXISTS lesson")

        Log.d(TAG, "Drop table lesson")
    }

	fun add(lesson: Lesson) : Long? {
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

    fun delete(id: Int) {
        database?.delete("lesson", "id = ?", arrayOf(id.toString()))

        Log.d(TAG, "Deleted row table: $id")
    }

    fun get(id: Int): Lesson {
        val arguments = arrayOf(id.toString())
        val cursor = database?.query("lesson", null, "id = ?", arguments, null, null, null)        
		
		try {
			if (cursor != null && cursor.moveToFirst()) {
				return Lesson(cursor.getInt(cursor.getColumnIndex("id")),
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

	override fun close() {
        if (instance != null) {
			database?.close()
		}            
    }
}