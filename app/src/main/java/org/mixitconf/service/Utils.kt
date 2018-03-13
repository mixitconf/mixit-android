package org.mixitconf.service

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.Html
import com.github.rjeschke.txtmark.Processor
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
    }
}

val SPECIAL_SLUG_CHARACTERS = mapOf<Char, Char>(Pair('é','e'), Pair('è','e'),Pair('ï','i'), Pair(' ','_'), Pair('ê','e'), Pair('à','a'), Pair('-','_'))

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