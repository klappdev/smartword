package org.kl.smartword.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper : SQLiteOpenHelper {
    private var database: SQLiteDatabase? = null

    constructor(context: Context?) : super(context, "smartword.db", null, 1) {
        database = this.writableDatabase
        LessonDB.database = database
        WordDB.database = database
    }

    override fun onCreate(database: SQLiteDatabase?) {
        LessonDB.create(database)
        WordDB.create(database)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        LessonDB.upgrade(database, oldVersion, newVersion)
        WordDB.upgrade(database, oldVersion, newVersion)
    }

    override fun close() {
        database?.close()
    }
}