package org.kl.smartword.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper : SQLiteOpenHelper {
    private var database: SQLiteDatabase? = null

    constructor(context: Context?) : super(context, "smartword.db", null, 1) {
        database = this.writableDatabase
        LessonDao.database = database
        WordDao.database = database
    }

    override fun onCreate(database: SQLiteDatabase?) {
        LessonDao.create(database)
        WordDao.create(database)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        LessonDao.upgrade(database, oldVersion, newVersion)
        WordDao.upgrade(database, oldVersion, newVersion)
    }

    override fun close() {
        database?.close()
    }
}