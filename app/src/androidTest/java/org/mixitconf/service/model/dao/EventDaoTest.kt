package org.mixitconf.service.model.dao

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mixitconf.createDate
import org.mixitconf.model.dao.EventDao
import org.mixitconf.model.dao.MiXiTDatabase
import org.mixitconf.model.entity.Event


@RunWith(AndroidJUnit4ClassRunner::class)
class EventDaoTest{

    private lateinit var database: MiXiTDatabase
    private lateinit var dao: EventDao
    private val event = Event("id", createDate(29, 8, 0) , createDate(30, 18, 0), 2020)

    @Before
    fun onInit(){
        val testContext = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(testContext, MiXiTDatabase::class.java).build()
        dao = database.eventDao()
        dao.create(event)
    }

    @After
    fun onClose() = database.close()

    @Test
    fun readAll(){
       Truth.assertThat(dao.readAll()).hasSize(1)
    }

    @Test
    fun readOne(){
        Truth.assertThat(dao.readOne(event.id)).isEqualTo(event)
    }

    @Test
    fun readOneByUnknownId(){
        Truth.assertThat(dao.readOne("dddd")).isNull()
    }

    @Test
    fun update(){
        val end = createDate(25, 18, 0)
        dao.update(event.copy(end = end))
        Truth.assertThat(dao.readOne(event.id).end).isEqualTo(end)
    }

    @Test
    fun delete(){
        dao.delete(event)
        Truth.assertThat(dao.readAll()).isEmpty()
    }
}