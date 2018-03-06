package org.mixitconf.mixitconf.repository

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
class UserReaderTest {
    lateinit var reader: UserReader
    lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getTargetContext()
        reader = UserReader(appContext)
    }

    @Test
    fun findAll() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertTrue(reader.findAll().size > 10)
    }

    @Test
    fun findOne() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val user = reader.findOne("romainguy@curious-creature.com")
        Assert.assertEquals("Guy", user.lastname)
        Assert.assertEquals("Romain", user.firstname)
    }

    @Test
    fun findByLogins() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val users = reader.findByLogins(listOf("romainguy@curious-creature.com", "graphicsgeek1@gmail.com"))
        Assert.assertEquals("Guy", users.get(0).lastname)
        Assert.assertEquals("Haase", users.get(1).lastname)
    }
}