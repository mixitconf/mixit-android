package org.mixitconf.service

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.widget.ImageView
import com.github.rjeschke.txtmark.Processor
import org.mixitconf.R
import org.mixitconf.model.User
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
        val OBJECT_ID = "id"
    }
}

val SPECIAL_SLUG_CHARACTERS = mapOf<Char, Char>(Pair('é','e'), Pair('è','e'),Pair('ï','i'), Pair(' ','_'), Pair('ê','e')
        , Pair('\'','_'), Pair('ô','o'), Pair('à','a'), Pair('-','_'))


// User extension
// ============================================
fun ImageView.setSpeakerImage(speaker: User) {
    // Speaker images are downloaded on the app startup
    val imageResource = context.resources.getIdentifier(
            "mxt_speker_${if(speaker.lastname.isNullOrEmpty()) speaker.firstname.toSlug() else speaker.lastname.toSlug()}",
            "drawable",
            context.applicationInfo.packageName)

    setImageResource(if (imageResource > 0) imageResource else R.drawable.mxt_icon_unknown)
}

// String extension to convert markdown to HTML
// ============================================
fun String.markdownToHtml() = if (isNullOrEmpty()) null else Processor.process(this).toHtml()

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun String.toHtml() = if (isNullOrEmpty()) null else Html.fromHtml(this)

fun String.toSlug(): String = toLowerCase().toCharArray().map { if (SPECIAL_SLUG_CHARACTERS.get(it) == null) it else SPECIAL_SLUG_CHARACTERS.get(it) }.joinToString("")

// Date extensions
// ============================================
fun Date.adjust(day: Int, hour: Int, minute: Int): Date {
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
    try{
        packageManager.getPackageInfo(type, 0)
        return true
    }
    catch (e:PackageManager.NameNotFoundException){
        return false
    }
}

// Fragment extensions
fun Fragment.withIdInBundle(id: String): Fragment {
    val args = Bundle()
    args.putString(Utils.OBJECT_ID, id)
    setArguments(args)
    return this
}