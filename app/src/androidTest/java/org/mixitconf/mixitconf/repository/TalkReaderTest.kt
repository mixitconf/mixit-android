package org.mixitconf.mixitconf.repository

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link TalkReader}
 */
@RunWith(AndroidJUnit4::class)
class TalkReaderTest {

    val reader = TalkReader()

    @Test
    fun findAll() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertTrue(reader.findAll(appContext).size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val talk = reader.findOne(appContext, "5a706948840deb00141a7240")
        assertEquals("Modern Android Development", talk.title)
    }

}