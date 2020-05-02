package org.mixitconf.model.dao

import androidx.room.*
import org.mixitconf.model.entity.Event

@Dao
interface EventDao {

    @Insert
    fun create(event: Event)

    @Query("select * from Event")
    fun readAll(): List<Event>

    @Query("select * from Event where id = :id")
    fun readOne(id: String): Event

    @Query("select * from Event where year = :year")
    fun readOneByYear(year: Int): Event?

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)

    @Query("delete from Event")
    fun deleteAll()
}
