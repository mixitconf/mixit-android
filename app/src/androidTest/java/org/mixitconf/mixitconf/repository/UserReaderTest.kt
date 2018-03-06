package org.mixitconf.mixitconf.repository

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test {@link UserReader}
 */
@RunWith(AndroidJUnit4::class)
class UserReaderTest {
    val reader = UserReader()

    @Test
    fun findAll() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertTrue(reader.findAll(appContext).size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val user = reader.findOne(appContext, "romainguy@curious-creature.com")
        Assert.assertEquals("Guy", user.lastname)
        Assert.assertEquals("Romain", user.firstname)
    }

    @Test
    fun findByLogins() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val users = reader.findByLogins(appContext, listOf("romainguy@curious-creature.com", "graphicsgeek1@gmail.com"))
        Assert.assertEquals("Guy", users.get(0).lastname)
        Assert.assertEquals("Haase", users.get(1).lastname)
    }
}