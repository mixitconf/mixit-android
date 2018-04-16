package org.mixitconf.repository

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
    private lateinit var reader: UserReader
    private lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getTargetContext()
        reader = UserReader.getInstance(appContext)
    }

    @Test
    fun findAll() {
        Assert.assertTrue(reader.findAll().size > 10)
    }

    @Test
    fun findOne() {
        val user = reader.findOne("romainguy")
        Assert.assertEquals("Guy", user.lastname)
        Assert.assertEquals("Romain", user.firstname)
    }

    @Test
    fun findByLogins() {
        val users = reader.findByLogins(listOf("romainguy", "graphicsgeek1"))
        Assert.assertEquals(2, users.size)
        users.forEach { Assert.assertTrue(it.lastname == "Guy" || it.lastname == "Haase") }
    }
}
