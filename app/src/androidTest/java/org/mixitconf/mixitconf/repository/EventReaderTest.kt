package org.mixitconf.mixitconf.repository

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link EventReader}
 */
@RunWith(AndroidJUnit4::class)
class EventReaderTest{
    val reader = EventReader()

    @Test
    fun findAll() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertTrue(reader.findAll(appContext).size == 7)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val event = reader.findOne(appContext, "mixit18")
        assertEquals(2018, event.year)
    }
}