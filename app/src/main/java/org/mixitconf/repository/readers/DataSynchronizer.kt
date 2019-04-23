package org.mixitconf.repository.readers

import android.content.Context
import androidx.room.Transaction
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mixitconf.R
import org.mixitconf.model.dto.EventDto
import org.mixitconf.model.dto.TalkDto
import org.mixitconf.model.dto.UserDto
import org.mixitconf.model.dto.toEntity
import org.mixitconf.repository.dao.MiXiTDatabase
import org.mixitconf.service.Constant
import kotlin.coroutines.CoroutineContext

class DataSynchronizer(val context: Context, val database: MiXiTDatabase) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    @Transaction
    fun initialize() {
        launch {

        }
    }

}