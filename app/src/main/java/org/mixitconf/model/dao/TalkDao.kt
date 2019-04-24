package org.mixitconf.model.dao

import androidx.room.*
import org.mixitconf.model.entity.Talk

@Dao
interface TalkDao {
    @Insert
    fun create(talk: Talk)

    @Query("select * from Talk")
    fun readAll(): List<Talk>

    @Query("select * from Talk where speakerIds like '%' || :id || '%'")
    fun readAllBySpeakerId(id: String): List<Talk>

    @Query("select * from Talk where id=:id")
    fun readOne(id: String): Talk?

    @Update
    fun update(talk: Talk)

    @Delete
    fun delete(talk: Talk)

    @Query("delete from Talk")
    fun deleteAll()

    @Query("delete from Talk where id in (:ids)")
    fun deleteAllById(ids: List<String>)
}
