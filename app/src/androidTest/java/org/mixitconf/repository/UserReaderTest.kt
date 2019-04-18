package org.mixitconf.repository

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
class UserReaderTest {
    private lateinit var reader: UserReader
    private lateinit var appContext: Context

    @Before
    fun init() {
        val mixitApp = InstrumentationRegistry.getInstrumentation().context.applicationContext as MiXiTApplication
        reader = mixitApp.userReader
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        Assert.assertTrue(reader.findAll().size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val user = reader.findOne("romainguy")
        Assert.assertEquals("Guy", user.lastname)
        Assert.assertEquals("Romain", user.firstname)
    }

    @Test
    fun findByLogins() {
        // Context of the app under test.
        val users = reader.findByLogins(listOf("romainguy", "graphicsgeek1"))
        users.forEach { Assert.assertTrue(it.lastname == "Guy" || it.lastname == "Haase") }
    }
}
