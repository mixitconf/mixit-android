package org.mixitconf.service

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Html
import android.widget.ImageView
import com.github.rjeschke.txtmark.Processor
import com.squareup.picasso.Picasso
import org.mixitconf.R
import org.mixitconf.model.Talk
import org.mixitconf.model.User
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        const val OBJECT_ID = "id"
    }
}

val SPECIAL_SLUG_CHARACTERS = mapOf(Pair('é','e'), Pair('è','e'),Pair('ï','i'), Pair(' ','_'), Pair('ê','e')
        , Pair('\'','_'), Pair('ô','o'), Pair('à','a'), Pair('-','_'))


// User extension
// ============================================
fun ImageView.setSpeakerImage(speaker: User) {
    // Speaker images are downloaded on the app startup
    val imageResource = context.resources.getIdentifier(
            "mxt_speker_${if(speaker.lastname.isEmpty()) speaker.firstname.toSlug() else speaker.lastname.toSlug()}",
            "drawable",
            context.applicationInfo.packageName)

    Picasso.get()
            .load(if (imageResource > 0) imageResource else R.drawable.mxt_icon_unknown)
            .resize(160, 160)
            .into(this)
}

fun Talk.getRoomLabel(context: Context): Int = context.resources.getIdentifier(room.name.toLowerCase(), "string", context.applicationInfo.packageName)
fun Talk.getTimeLabel(context: Context): String = String.format(
        context.resources.getString(R.string.talk_time_range),
        Utils.DATE_FORMAT.format(start),
        DateFormat.getTimeInstance(DateFormat.SHORT).format(start),
        DateFormat.getTimeInstance(DateFormat.SHORT).format(end))
fun Talk.getBgColorDependingOnTime(color: Int):Int = if(Date().time > end.time) R.color.unknown else color

fun User.fullname(): String = "$firstname $lastname".trim()

// String extension to convert markdown to HTML
// ============================================
fun String.markdownToHtml() = if (isNullOrEmpty()) null else Processor.process(this).toHtml()

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun String.toHtml() = if (isNullOrEmpty()) null else Html.fromHtml(this)

fun String.toSlug(): String = toLowerCase().toCharArray().map { if (SPECIAL_SLUG_CHARACTERS[it] == null) it else SPECIAL_SLUG_CHARACTERS[it] }.joinToString("")

// Date extensions
// ============================================
fun createDate(day: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.set(2018, 3, day, hour, minute, 0)
    return calendar.time
}

fun Date.toLocale(): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.time = this
    calendar.add(Calendar.HOUR, -2)
    return calendar.time
}

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
fun Context.hasPermission(permission: String):Boolean = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun Context.getColorLegacy(color:Int) = resources.getColor(color)

// Fragment extensions
// =============================================
fun Fragment.withIdInBundle(id: String): Fragment {
    val args = Bundle()
    args.putString(Utils.OBJECT_ID, id)
    arguments = args
    return this
}
fun FragmentManager.openFragmentDetail(id: String, fragment: Fragment):Int = beginTransaction().apply {

    fragment.withIdInBundle(id)
    replace(R.id.container, fragment)
    addToBackStack(fragment.tag)
}.commit()
fun FragmentManager.openFragment(fragment: Fragment):Int = beginTransaction().apply {
    replace(R.id.container, fragment)
    addToBackStack(fragment.tag)
}.commit()