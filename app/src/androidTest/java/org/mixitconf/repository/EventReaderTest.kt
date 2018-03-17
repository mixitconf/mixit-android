package org.mixitconf.repository

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link EventReader}
 */
@RunWith(AndroidJUnit4::class)
class EventReaderTest {
    private lateinit var reader: EventReader
    private lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getTargetContext()
        reader = EventReader(appContext)
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        assertTrue(reader.findAll().size == 7)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val event = reader.findOne("mixit18")
        assertEquals(2018, event.year)
    }
}