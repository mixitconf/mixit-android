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
 * Test {@link EventReader}
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class EventReaderTest {
    private lateinit var reader: EventReader

    @Before
    fun init() {
        val mixitApp = InstrumentationRegistry.getInstrumentation().context.applicationContext as MiXiTApplication
        reader = mixitApp.eventReader
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
        assertEquals(2019, event.year)
    }
}