package org.kl.smartword

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.kl.smartword.bean.Lesson
import org.kl.smartword.db.LessonDB
import org.kl.smartword.util.formatted
import java.util.*

/*
    val lessonDB = LessonDB.getInstance(fragmentContext)

    lessonDB.add(Lesson(1, R.drawable.lesson_background_1, "Geometry", "", DateUtil.currentDateTime(), false))
    lessonDB.add(Lesson(1, R.drawable.lesson_background_2, "Music", "", DateUtil.currentDateTime(), false))
    lessonDB.add(Lesson(1, R.drawable.lesson_background_3, "Arithmetic", "", DateUtil.currentDateTime(), false))
    lessonDB.add(Lesson(1, R.drawable.lesson_background_4, "Computer", "", DateUtil.currentDateTime(), false))
    lessonDB.add(Lesson(1, R.drawable.lesson_background, "Drawing", "", DateUtil.currentDateTime(), false))
 */
@RunWith(AndroidJUnit4::class)
class LessonDBInstrumentedTest {
    private var rowId: Long? = 0

    companion object {
        @JvmStatic
        private lateinit var appContext: Context
        @JvmStatic
        private lateinit var lessonDB: LessonDB

        @BeforeClass
        @JvmStatic
        fun setup() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext

            lessonDB = LessonDB.getInstance(appContext)
        }

        @AfterClass
        @JvmStatic
        fun release() {
            this.lessonDB.close()
        }

    }

    @Test
    fun addLessonTest() {
        val expectedLesson = Lesson(1, R.drawable.lesson_background_1, "Geometry", "",
                                    Date().formatted(), false)
        rowId = lessonDB.add(expectedLesson)
        expectedLesson.id = rowId!!.toInt()

        val actualLesson = lessonDB.get(rowId!!.toInt())

        assertEquals(expectedLesson, actualLesson)
    }

    @Test
    fun updateLessonTest() {
        val expectedLesson = lessonDB.get(rowId!!.toInt())
        expectedLesson.icon = R.drawable.lesson_background_2
        expectedLesson.name = "Music"
        expectedLesson.description = "Music lesson"

        lessonDB.update(expectedLesson)

        val actualLesson = lessonDB.get(rowId!!.toInt())

        assertEquals(expectedLesson, actualLesson)
    }
}
