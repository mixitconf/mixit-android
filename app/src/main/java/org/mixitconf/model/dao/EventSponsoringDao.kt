package org.mixitconf.model.dao

import androidx.room.*
import org.mixitconf.model.entity.EventSponsoring

@Dao
interface EventSponsoringDao {

    @Insert
    fun create(eventSponsoring: EventSponsoring)

    @Query("select * from EventSponsoring")
    fun readAll(): List<EventSponsoring>

    @Query("select * from EventSponsoring where sponsorId = :sponsorId and eventId= :eventId")
    fun readOne(sponsorId: String, eventId: String): EventSponsoring

    @Update
    fun update(eventSponsoring: EventSponsoring)

    @Delete
    fun delete(eventSponsoring: EventSponsoring)

    @Query("delete from EventSponsoring")
    fun deleteAll()
}
