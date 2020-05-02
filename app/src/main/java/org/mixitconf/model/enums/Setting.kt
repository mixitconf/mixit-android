package org.mixitconf.model.enums

enum class SettingValue(val key: String) {
    DATA_AUTO_SYNC_ENABLE("sync_data"),
    WS_FAVORITE_SYNC_ENABLE("sync_favorite"),
    EMAIL_SYNC("sync_email"),
    FAVORITE_NOTIFICATION_ENABLE("favorite_notification"),
    FAVORITE_NOTIFICATION_DURATION("favorite_notification_minute"),
    DATA_INITIALIZED("data_2020_initialized"),
    FAVORITES_POSITION("favorites_position"),
    TALKS_POSITION("talks_position")
}

