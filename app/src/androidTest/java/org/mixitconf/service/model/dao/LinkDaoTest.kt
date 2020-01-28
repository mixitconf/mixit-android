package org.mixitconf.service.model.dao

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mixitconf.model.dao.LinkDao
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.model.entity.Link
import org.mixitconf.model.enums.LinkType


@RunWith(AndroidJUnit4ClassRunner::class)
class LinkDaoTest {

    private lateinit var database: MiXiTDatabase
    private lateinit var dao: LinkDao
    private val element = Link("twitter", "https://twitter.com/guillaumeehret", "1", LinkType.Social)

    @Before
    fun onInit() {
        val testContext = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(testContext, MiXiTDatabase::class.java).build()
        dao = database.linkDao()
        dao.create(element)
    }

    @After
    fun onClose() = database.close()

    @Test
    fun readAll() {
        Truth.assertThat(dao.readAll()).hasSize(1)
    }

    @Test
    fun readOne() {
        Truth.assertThat(dao.readOne(element.id)).isEqualTo(element)
    }

    @Test
    fun readAllBySpeakerId() {
        Truth.assertThat(dao.readAllBySpeakerId(element.speakerId)).hasSize(1)
    }

    @Test
    fun readOneByUnknownId() {
        Truth.assertThat(dao.readOne("dddd")).isNull()
    }

    @Test
    fun update() {
        dao.update(element.copy(name = "social"))
        Truth.assertThat(dao.readOne(element.id).name).isEqualTo("social")
    }

    @Test
    fun delete() {
        dao.delete(element)
        Truth.assertThat(dao.readAll()).isEmpty()
    }

    @Test
    fun deleteBySpeaker() {
        dao.deleteBySpeaker(element.speakerId)
        Truth.assertThat(dao.readAll()).isEmpty()
    }

}