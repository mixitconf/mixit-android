package org.mixitconf.model.enums

import org.mixitconf.R

enum class TalkFormat(val duration: Int, val label: Int) {
    TALK(50, R.string.TALK),
    WORKSHOP(110, R.string.WORKSHOP),
    RANDOM(25, R.string.RANDOM),
    KEYNOTE(25, R.string.KEYNOTE),
    KEYNOTE_SURPRISE(25, R.string.KEYNOTE_SURPRISE),

    WELCOME(45, R.string.planning_reception),
    SESSION_INTRO(15, R.string.planning_session_intro),
    PAUSE_10_MIN(10, R.string.planning_pause_10),
    PAUSE_25_MIN(25, R.string.planning_pause_25),
    PAUSE_30_MIN(30, R.string.planning_pause_30),
    PARTY(210, R.string.party),
    LUNCH(90, R.string.planning_lunch),
    ORGA(15, R.string.planning_team_word),
    DAY(0,R.string.app_name);
}

fun TalkFormat.isTalk(): Boolean =
    this == TalkFormat.TALK || this == TalkFormat.WORKSHOP || this == TalkFormat.RANDOM || this == TalkFormat.KEYNOTE || this == TalkFormat.KEYNOTE_SURPRISE
