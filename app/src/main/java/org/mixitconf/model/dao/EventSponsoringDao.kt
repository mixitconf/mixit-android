package org.mixitconf.model.dao

import androidx.room.*
import org.mixitconf.model.entity.EventSponsoring

@Dao
interface EventSponsoringDao {

    @Insert
    fun create(eventSponsoring: EventSponsoring)

    @Query("select * from EventSponsoring")
    fun readAll(): List<EventSponsoring>

    @Query("select * from EventSponsoring where sponsorId = :id")
    fun readOne(id: String): EventSponsoring

    @Update
    fun update(eventSponsoring: EventSponsoring)

    @Delete
    fun delete(eventSponsoring: EventSponsoring)
}
