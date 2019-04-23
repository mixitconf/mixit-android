package org.mixitconf.repository.dao

import androidx.room.*
import org.mixitconf.model.entity.Speaker

@Dao
interface SpeakerDao {
    @Insert
    fun create(speaker: Speaker)

    @Query("select * from Speaker")
    fun readAll(): List<Speaker>

    @Query("select * from Speaker where login in (:ids)")
    fun readAllByIds(ids: List<String>): List<Speaker>

    @Query("select * from Speaker where login = :login")
    fun readOne(login: String): Speaker

    @Update
    fun update(speaker: Speaker)

    @Delete
    fun delete(speaker: Speaker)
}
