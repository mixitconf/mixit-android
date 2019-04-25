package org.mixitconf.model.entity

import android.net.Uri
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.github.rjeschke.txtmark.Processor
import com.squareup.picasso.Picasso
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.model.enums.LinkType
import org.mixitconf.toHtml

@Entity
data class Speaker(
    @PrimaryKey
    val login: String,
    val firstname: String,
    val lastname: String,
    val company: String?,
    val descriptionFr: String?,
    val descriptionEn: String?
) {
    // These list is only populated when we want to see the speaker detail
    @Ignore
    val links: MutableList<Link> = mutableListOf()
    @Ignore
    val talks: MutableList<Talk> = mutableListOf()
}

val Speaker.fullname
    get() = "$firstname $lastname".trim()

val Speaker.descriptionInMarkdown
    get() = if (descriptionFr.isNullOrEmpty()) null else Processor.process(descriptionFr).toHtml()

private val Speaker.socialLink: Link?
    get() = if(links.isEmpty()) null else links.firstOrNull { it.linkType == LinkType.Social }

private val Speaker.webLink: Link?
    get() = if(links.isEmpty()) null else links.firstOrNull { it.linkType != LinkType.Social }

val Speaker.hasLink
    get() = socialLink != null || webLink != null

val Speaker.linkUri
    get() = Uri.parse(socialLink?.url ?: webLink?.url)

val Speaker.imageLinkResourceId
    get() = if (socialLink != null) socialLink!!.socialType!!.resourceId else R.drawable.mxt_icon_web


fun ImageView.setSpeakerImage(speaker: Speaker) {
    // Speaker images are downloaded on the app startup
    val imageResource = context.resources.getIdentifier(
        speaker.login.toSlug().toValidImageName().toLowerCase(),
        "drawable",
        context.applicationInfo.packageName
    )

    Picasso.get()
        .load(if (imageResource > 0) imageResource else R.drawable.mxt_icon_unknown)
        .resize(160, 160)
        .into(this)
}

private fun String.toSlug(): String =
    toLowerCase().toCharArray().map { if (MiXiTApplication.SPECIAL_SLUG_CHARACTERS[it] == null) it else MiXiTApplication.SPECIAL_SLUG_CHARACTERS[it] }.joinToString(
        ""
    )

private fun String.toValidImageName(): String = if (this.startsWith("1")) "S$this" else this
