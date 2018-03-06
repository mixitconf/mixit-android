package org.mixitconf.mixitconf.repository

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link TalkReader}
 */
@RunWith(AndroidJUnit4::class)
class TalkReaderTest {

    lateinit var reader: TalkReader
    lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getTargetContext()
        reader = TalkReader(appContext)
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertTrue(reader.findAll().size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val talk = reader.findOne("5a706948840deb00141a7240")
        assertEquals("Modern Android Development", talk.title)
    }

}