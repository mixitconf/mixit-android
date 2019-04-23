package org.mixitconf.repository.dao

import androidx.room.*
import org.mixitconf.model.entity.Link

@Dao
interface LinkDao {
    @Insert
    fun create(link: Link)

    @Query("select * from Link")
    fun readAll(): List<Link>

    @Query("select * from Link where id = :id")
    fun readOne(id: String): Link

    @Update
    fun update(link: Link)

    @Delete
    fun delete(link: Link)
}
