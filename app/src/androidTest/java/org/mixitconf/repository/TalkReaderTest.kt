package org.mixitconf.repository

import android.content.Context
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mixitconf.MiXiTApplication

/**
 * Test {@link TalkReader}
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class TalkReaderTest {

    private lateinit var reader: TalkReader
    private lateinit var appContext: Context

    @Before
    fun init() {
        val mixitApp = InstrumentationRegistry.getInstrumentation().context.applicationContext as MiXiTApplication
        reader = mixitApp.talkReader
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        assertTrue(reader.findAll().size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val talk = reader.findOne("5a706948840deb00141a7240")
        assertEquals("Modern Android Development", talk.title)
    }

}