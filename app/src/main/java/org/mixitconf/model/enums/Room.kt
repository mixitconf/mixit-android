package org.mixitconf.model.enums

import org.mixitconf.R

enum class Room(val i18nId: Int, val capacity: Int, val filmed: Boolean=false, val risp:Boolean=false, val scribo:Boolean=false, val scriboUrl:String?=null) {
    AMPHI1(R.string.amphi1, 500, filmed=true, risp = true),
    AMPHI2(R.string.amphi2, 200, filmed=true, risp = true),
    AMPHIC(R.string.amphic, 445, filmed=true, risp = true),
    AMPHID(R.string.amphid, 445, filmed=true, scribo = true, scriboUrl = "https://mixit-2019.appspot.com/root/conf/conf_lecteur_tele.html#fr-FR_conf01"),
    AMPHIK(R.string.amphik, 300, filmed=true, risp = true),
    SPEAKER(R.string.speaker, 16),
    ROOM1(R.string.room1, 198, filmed=true, scribo = true, scriboUrl = "https://mixit-2019.appspot.com/root/conf/conf_lecteur_tele.html#fr-FR_conf02"),
    ROOM2(R.string.room2, 108),
    ROOM3(R.string.room3, 30),
    ROOM4(R.string.room4, 30),
    ROOM5(R.string.room5, 30),
    ROOM6(R.string.room6, 30),
    ROOM7(R.string.room7, 30),
    OUTSIDE(R.string.outside, 40),
    MUMMY(R.string.mummy, 2),
    UNKNOWN(R.string.unknown, 0),
    SURPRISE(R.string.surprise, 0)
}

val Room.hardOfHearingSytem
    get() = this.risp || this.scribo
