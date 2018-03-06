package org.mixitconf.mixitconf.service

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link UserReader}
 */
@RunWith(AndroidJUnit4::class)
class SpeakerServiceTest{
    lateinit var service: SpeakerService
    lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getTargetContext()
        service = SpeakerService(appContext)
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        Assert.assertEquals(95, service.findSpeakers().size)
    }
}