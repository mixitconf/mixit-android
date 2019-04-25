package org.mixitconf.service.model.dao

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mixitconf.model.dao.EventSponsoringDao
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.model.entity.EventSponsoring
import org.mixitconf.model.enums.SponsorshipLevel


@RunWith(AndroidJUnit4ClassRunner::class)
class EventSponsoringDaoTest {

    lateinit var database: MiXiTDatabase
    lateinit var dao: EventSponsoringDao
    private val element = EventSponsoring("sponsorId", "2019", SponsorshipLevel.GOLD)

    @Before
    fun onInit() {
        val testContext = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(testContext, MiXiTDatabase::class.java).build()
        dao = database.eventSponsoringDao()
        dao.create(element)
    }

    @After
    fun onClose() = database.close()

    @Test
    fun readAll() {
        Truth.assertThat(dao.readAll()).hasSize(1);
    }

    @Test
    fun readOne() {
        Truth.assertThat(dao.readOne(element.sponsorId, "2019")).isEqualTo(element);
    }

    @Test
    fun readOneByUnknownId() {
        Truth.assertThat(dao.readOne("dddd", "eee")).isNull();
    }

    @Test
    fun delete() {
        dao.delete(element)
        Truth.assertThat(dao.readAll()).isEmpty()
    }
}