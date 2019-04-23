package org.mixitconf.service

import java.text.SimpleDateFormat
import java.util.*

class Constant {
    companion object {
        val CURRENT_EDITION = 2019
        val SPECIAL_SLUG_CHARACTERS = mapOf(
            Pair('é', 'e'), Pair('è', 'e'), Pair('ï', 'i'), Pair(' ', '_'), Pair('ê', 'e'), Pair('.', '_')
            , Pair('\'', '_'), Pair('ô', 'o'), Pair('à', 'a'), Pair('-', '_')
        )

        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        val OBJECT_ID = "id"
    }
}