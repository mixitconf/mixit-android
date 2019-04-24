package org.mixitconf.model.enums

import org.mixitconf.R

enum class Social(val pattern: String, val resourceId: Int) {
    TWITTER("twitter", R.drawable.mxt_icon_twitter_zoom),
    GITHUB("github", R.drawable.mxt_icon_github_zoom),
    LINKEDIN("linkedin", R.drawable.mxt_icon_linkedin_zoom)
}