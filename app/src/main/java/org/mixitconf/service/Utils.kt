package org.mixitconf.service

import android.text.Html
import com.github.rjeschke.txtmark.Processor
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())
    }
}

fun String.markdownToHtml() = if(isNullOrEmpty()) null else Processor.process(this).toHtml()
fun String.toHtml() = if(isNullOrEmpty()) null else Html.fromHtml(this)

