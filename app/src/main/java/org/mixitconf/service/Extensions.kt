package org.mixitconf.service

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.mixitconf.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


// String extension to convert markdown to HTML
// ============================================
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun String.toHtml() = if (isNullOrEmpty()) null else Html.fromHtml(this)



// Date extensions
// ============================================
fun createDate(day: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.set(2019, 4, day, hour, minute, 0)
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

// Fragment extensions
// =============================================

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

fun <T: RecyclerView.ViewHolder> RecyclerView.default(init: () -> RecyclerView.Adapter<T>) = this.apply {
    setHasFixedSize(true)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    layoutManager = LinearLayoutManager(context)
    adapter  = init()
    return this
}


val Boolean.visibility
        get() = if(this) View.VISIBLE else View.GONE