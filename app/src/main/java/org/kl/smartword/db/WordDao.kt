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

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.Completable
import io.reactivex.Observable

import timber.log.Timber
import org.kl.smartword.model.Word

class WordDao {
    internal var database: SQLiteDatabase? = null

	fun create(database: SQLiteDatabase?) {
        database?.execSQL("""CREATE TABLE IF NOT EXISTS word (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
							 id_lesson INTEGER NOT NULL, 
                             name TEXT NOT NULL,
                             transcription TEXT NOT NULL,
                             translation TEXT NOT NULL,							 
							 association TEXT NOT NULL,
							 etymology TEXT NOT NULL,
							 description TEXT NOT NULL,
                             date TEXT NOT NULL);
                          """)

        Timber.d("Create table word")
    }
	
	fun upgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		if (oldVersion != newVersion) {            
            database?.execSQL("DROP TABLE IF EXISTS word")
            create(database)
        }

        Timber.d("Upgrade table word")
	}

    private fun addSynchronously(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("description", word.description)
        values.put("date", word.date)

        val rowId = database?.insert("word", null, values)

        Timber.d("Inserted new row table: $rowId")
    }

    fun add(word: Word): Completable {
        return Completable.fromRunnable {
            addSynchronously(word)
        }
    }

    fun addAll(words: List<Word>): Completable {
        return Completable.fromRunnable {
            words.forEach(::addSynchronously)
        }
    }

    private fun updateSynchronously(word: Word) {
        val values = ContentValues()
        values.put("id_lesson", word.idLesson)
        values.put("name", word.name)
        values.put("transcription", word.transcription)
        values.put("translation", word.translation)
        values.put("association", word.association)
        values.put("etymology", word.etymology)
        values.put("description", word.description)
        values.put("date", word.date)

        database?.update("word", values, "id = ?", arrayOf(word.id.toString()))

        Timber.d("Updated row table: ${word.id}")
    }

    fun update(word: Word): Completable {
        return Completable.fromRunnable {
            updateSynchronously(word)
        }
    }

    private fun deleteSynchronously(id: Long) {
        database?.delete("word", "id = ?", arrayOf(id.toString()))

        Timber.d("Deleted row table: $id")
    }

    fun delete(id: Long): Completable {
        return Completable.fromRunnable {
            deleteSynchronously(id)
        }
    }

    private fun checkIfEmptySynchronously(): Boolean {
        var result = false
        val cursor = database?.rawQuery("SELECT COUNT(*) FROM word", null)

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
        val cursor = database?.rawQuery("SELECT COUNT(*) FROM word WHERE name=?", arrayOf(name.trim()))

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
                            cursor.getString(cursor.getColumnIndex("association")),
                            cursor.getString(cursor.getColumnIndex("etymology")),
                            cursor.getString(cursor.getColumnIndex("description")),
                            cursor.getString(cursor.getColumnIndex("date"))
                )
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Retrieve row table by id: $id")

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
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Retrieve all rows table")

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
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Search all rows table by id lesson")

        return words
    }

    fun getAllByIdLesson(idLesson: Long): Observable<List<Word>> {
        return Observable.fromCallable {
            getAllByIdLessonSynchronously(idLesson)
        }
    }

    private fun searchByNameSynchronously(name: String, idLesson: Long): List<Word> {
        val words = mutableListOf<Word>()
        val arguments = arrayOf(idLesson.toString(), name)
        val cursor = database?.query("word", null, "id_lesson = ? AND name LIKE '%' || ? || '%'",
                                     arguments, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("transcription")),
                        cursor.getString(cursor.getColumnIndex("translation")),
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Search all rows table by name: $name")

        return words
    }

    fun searchByName(name: String, idLesson: Long): Observable<List<Word>> {
        return Observable.fromCallable {
            searchByNameSynchronously(name, idLesson)
        }
    }

    private fun sortByNameSynchronously(idLesson: Long, asc: Boolean): List<Word> {
        val words = mutableListOf<Word>()
        val arguments = arrayOf(idLesson.toString())
        val orderBy = if (asc) "name ASC" else "name DESC"
        val cursor = database?.query("word", null, "id_lesson = ?", arguments, null, null, orderBy)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    words += Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getLong(cursor.getColumnIndex("id_lesson")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("transcription")),
                        cursor.getString(cursor.getColumnIndex("translation")),
                        cursor.getString(cursor.getColumnIndex("association")),
                        cursor.getString(cursor.getColumnIndex("etymology")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("date"))
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        Timber.d("Sort all rows table by $orderBy")

        return words
    }

    fun sortByName(idLesson: Long, asc: Boolean): Observable<List<Word>> {
        return Observable.fromCallable {
            sortByNameSynchronously(idLesson, asc)
        }
    }
}