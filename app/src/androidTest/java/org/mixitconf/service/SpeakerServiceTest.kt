package org.mixitconf.service

import android.content.Context
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mixitconf.MiXiTApplication

/**
 * Test {@link UserReader}
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class SpeakerServiceTest{
    private lateinit var service: SpeakerService
    private lateinit var appContext: Context

    @Before
    fun init() {
        val mixitApp = InstrumentationRegistry.getInstrumentation().context.applicationContext as MiXiTApplication
        service = mixitApp.speakerService
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        val speakers = service.findSpeakers()
        Assert.assertTrue(speakers.size > 50)

        speakers.forEach { System.out.println(it.photoUrl) }
    }
}