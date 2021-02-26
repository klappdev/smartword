/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2021 https://github.com/klappdev
 *
 * Permission is hereby  granted, free of charge, to any  person obtaining a copy
 * of this software and associated  documentation files (the "Software"), to deal
 * in the Software  without restriction, including without  limitation the rights
 * to  use, copy,  modify, merge,  publish, distribute,  sublicense, and/or  sell
 * copies  of  the Software,  and  to  permit persons  to  whom  the Software  is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE  IS PROVIDED "AS  IS", WITHOUT WARRANTY  OF ANY KIND,  EXPRESS OR
 * IMPLIED,  INCLUDING BUT  NOT  LIMITED TO  THE  WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR  A PARTICULAR PURPOSE AND  NONINFRINGEMENT. IN NO EVENT  SHALL THE
 * AUTHORS  OR COPYRIGHT  HOLDERS  BE  LIABLE FOR  ANY  CLAIM,  DAMAGES OR  OTHER
 * LIABILITY, WHETHER IN AN ACTION OF  CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE  OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kl.smartword.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

import timber.log.Timber
import org.kl.smartword.model.Lesson

class LessonDao {
    internal var database: SQLiteDatabase? = null
    internal var dataSubject: BehaviorSubject<List<Lesson>> = BehaviorSubject.create()

    fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS lesson (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             name TEXT NOT NULL,
                             description TEXT NOT NULL,
                             date TEXT NOT NULL,
                             icon_url TEXT NOT NULL);
                          """
        )

        Timber.d("Create table lesson")
    }

    fun upgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            database?.execSQL("DROP TABLE IF EXISTS lesson")
            create(database)
        }

        Timber.d("Upgrade table lesson")
    }

    private fun addSynchronously(lesson: Lesson) {
        val values = ContentValues()
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("icon_url", lesson.iconUrl)

        val rowId: Long? = database?.insert("lesson", null, values)

        Timber.d("Inserted new row table: $rowId")
    }

    fun add(lesson: Lesson): Completable {
        return Completable.fromRunnable {
            addSynchronously(lesson)
        }
    }

    fun addAll(lessons: List<Lesson>): Completable {
        return Completable.fromRunnable {
            lessons.forEach(::addSynchronously)
        }
    }

    private fun updateSynchronously(lesson: Lesson) {
        val values = ContentValues()
        values.put("name", lesson.name)
        values.put("description", lesson.description)
        values.put("date", lesson.date)
        values.put("icon_url", lesson.iconUrl)

        database?.update("lesson", values, "id = ?", arrayOf(lesson.id.toString()))

        Timber.d("Updated row table: ${lesson.id}")
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

        Timber.d("Deleted row table: $id")
    }

    fun delete(id: Long): Completable {
        return Completable.fromRunnable {
            deleteSynchronously(id)
        }
    }

    private fun checkIfEmptySynchronously(): Boolean {
        var result = false
        val cursor = database?.rawQuery("SELECT COUNT(*) FROM lesson", null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = (cursor.getInt(0) == 0)
            }
        } finally {
            cursor?.close()
        }

        return result
    }

    fun checkIfEmpty(): Single<Boolean> {
        return Single.fromCallable {
            checkIfEmptySynchronously()
        }
    }

    private fun checkIfExistsSynchronously(name: String): Boolean {
        var result = false
        val cursor = database?.rawQuery("SELECT COUNT(*) FROM lesson WHERE name=?", arrayOf(name.trim()))

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = (cursor.getInt(0) != 0)
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

        Timber.d("Retrieve all row table by id: $id")

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

        Timber.d("Retrieve all rows table")

        return lessons
    }

    fun getAll(): Observable<List<Lesson>> {
        return Observable.fromCallable(::getAllSynchronously)
    }

    private fun getAllIdsSynchronously(): List<Long> {
        val listIds = mutableListOf<Long>()
        val column = arrayOf("id")
        val cursor: Cursor? = database?.query("lesson", column, null, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    listIds += cursor.getLong(cursor.getColumnIndex("id"))
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Retrieve all ids table")

        return listIds
    }

    fun getAllIds(): Observable<List<Long>> {
        return Observable.fromCallable(::getAllIdsSynchronously)
    }

    private fun searchByNameSynchronously(name: String): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val arguments = arrayOf(name)
        val cursor = database?.query("lesson", null, "name LIKE '%' || ? || '%'", arguments, null, null, null)

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

        Timber.d("Search all rows table by name: $name")

        return lessons
    }

    fun searchByName(name: String): Observable<List<Lesson>> {
        return Observable.fromCallable {
            searchByNameSynchronously(name)
        }
    }

    private fun sortByNameSynchronously(asc: Boolean): List<Lesson> {
        val lessons = mutableListOf<Lesson>()
        val orderBy = if (asc) "name ASC" else "name DESC"
        val cursor = database?.query("lesson", null, null, null, null, null, orderBy)

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

        Timber.d("Sort all rows table by $orderBy")

        return lessons
    }

    fun sortByName(asc: Boolean): Observable<List<Lesson>> {
        return Observable.fromCallable {
            sortByNameSynchronously(asc)
        }
    }
}