package org.mixitconf.service

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.rjeschke.txtmark.Processor
import com.squareup.picasso.Picasso
import org.mixitconf.MiXiTApplication
import org.mixitconf.R
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat
import org.mixitconf.model.User
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Constant {
    companion object {
        val SPECIAL_SLUG_CHARACTERS = mapOf(
            Pair('é', 'e'), Pair('è', 'e'), Pair('ï', 'i'), Pair(' ', '_'), Pair('ê', 'e')
            , Pair('\'', '_'), Pair('ô', 'o'), Pair('à', 'a'), Pair('-', '_')
        )

        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        val OBJECT_ID = "id"
    }
}


// User extension
// ============================================
fun ImageView.setSpeakerImage(speaker: User) {
    // Speaker images are downloaded on the app startup
    val imageResource = context.resources.getIdentifier(
        "mxt_speker_${if (speaker.lastname.trim().isEmpty()) speaker.firstname.toSlug() else speaker.lastname.toSlug()}",
        "drawable",
        context.applicationInfo.packageName
    )

    Picasso.get()
        .load(if (imageResource > 0) imageResource else R.drawable.mxt_icon_unknown)
        .resize(160, 160)
        .into(this)
}

val Date.formatToShort
    get() = DateFormat.getTimeInstance(DateFormat.SHORT).format(this)

fun Talk.getTimeLabel(resources: Resources): String = String.format(
    resources.getString(R.string.talk_time_range),
    Constant.DATE_FORMAT.format(start),
    DateFormat.getTimeInstance(DateFormat.SHORT).format(startLocale()),
    DateFormat.getTimeInstance(DateFormat.SHORT).format(endLocale())
)

fun Talk.startLocale(): Date = if (format.isTalk()) start.inFrenchLocale else start
fun Talk.endLocale(): Date = if (format.isTalk()) end.inFrenchLocale else end

fun Talk.getBgColorDependingOnTime(color: Int): Int = if (Date().time > end.time) R.color.unknown else color

fun TalkFormat.isTalk(): Boolean =
    this == TalkFormat.TALK || this == TalkFormat.WORKSHOP || this == TalkFormat.RANDOM || this == TalkFormat.KEYNOTE

fun User.fullname(): String = "$firstname $lastname".trim()

// String extension to convert markdown to HTML
// ============================================
fun String.markdownToHtml() = if (isNullOrEmpty()) null else Processor.process(this).toHtml()

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun String.toHtml() = if (isNullOrEmpty()) null else Html.fromHtml(this)

fun String.toSlug(): String =
    toLowerCase().toCharArray().map { if (Constant.SPECIAL_SLUG_CHARACTERS[it] == null) it else Constant.SPECIAL_SLUG_CHARACTERS[it] }.joinToString(
        ""
    )

// Date extensions
// ============================================
fun createDate(day: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.set(2019, 4, day, hour, minute, 0)
    return calendar.time
}

val Date.inFrenchLocale
    get(): Date {
        val calendar = Calendar.getInstance(Locale.FRANCE)
        calendar.time = this
        calendar.add(Calendar.HOUR, -2)
        return calendar.time
    }

val Date.timeInFrenchLocale
    get() = this.inFrenchLocale.time

fun Date.addMinutes(amount: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.time = this
    calendar.add(Calendar.MINUTE, amount)
    return calendar.time
}

// Context extension
// ============================================
fun Context.hasIntentPackage(type: String): Boolean {
    try {
        packageManager.getPackageInfo(type, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
    return true
}

// Fragment extensions
// =============================================
val Fragment.mixitApp
    get() = this.activity?.application as MiXiTApplication

fun Fragment.withIdInBundle(id: String): Fragment {
    val args = Bundle()
    args.putString(Constant.OBJECT_ID, id)
    arguments = args
    return this
}


fun AppCompatActivity.openFragmentDetail(id: String, fragment: Fragment): Int =
    this.supportFragmentManager
        .beginTransaction()
        .apply {
            fragment.withIdInBundle(id)
            replace(R.id.container, fragment)
            addToBackStack(fragment.tag)
        }
        .commit()

fun AppCompatActivity.openFragment(fragment: Fragment): Int =
    this.supportFragmentManager
        .beginTransaction()
        .apply {
            replace(R.id.container, fragment)
            addToBackStack(fragment.tag)
        }
        .commit()

// Extension to not use deprecated methods in code
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun Resources.getLegacyColor(id: Int) = this.getColor(id)