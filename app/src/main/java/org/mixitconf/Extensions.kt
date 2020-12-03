package org.mixitconf

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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.mixitconf.model.enums.SettingValue
import retrofit2.Call
import java.util.*


// String extension to convert markdown to HTML
// ============================================
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun String?.toHtml() = if (this == null) {
    ""
} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
} else {
    Html.fromHtml(this)
}

// Extension to not use deprecated methods in code
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
fun Resources.getLegacyColor(id: Int) = this.getColor(id)


// Date extensions
// ============================================
fun createDate(day: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.set(2020, 3, day, hour, minute, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.addMinutes(amount: Int): Date {
    val calendar = Calendar.getInstance(Locale.FRANCE)
    calendar.time = this
    calendar.add(Calendar.MINUTE, amount)
    return calendar.time
}

val Date.durationInText: String
    get() = ((this.time - (Date().time) / 1000)).let {
        if (it < 0) {
            return "0 sec"
        }
        if (it < 60) {
            return "${it} sec."
        }
        return "${it / 60} min"
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
    args.putString(MiXiTApplication.OBJECT_ID, id)
    arguments = args
    return this
}


fun AppCompatActivity.openFragmentDetail(id: String, fragment: Fragment): Int = this.supportFragmentManager.beginTransaction().apply {
    fragment.withIdInBundle(id)
    replace(R.id.container, fragment)
    addToBackStack(fragment.tag)
}.commit()

fun AppCompatActivity.openFragment(fragment: Fragment): Int = this.supportFragmentManager.beginTransaction().apply {
    replace(R.id.container, fragment)
    addToBackStack(fragment.tag)
}.commit()


fun <T : RecyclerView.ViewHolder> RecyclerView.default(init: () -> RecyclerView.Adapter<T>) = this.apply {
    setHasFixedSize(true)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    layoutManager = LinearLayoutManager(context)
    adapter = init()
    return this
}


val Boolean.visibility
    get() = if (this) View.VISIBLE else View.GONE

// Read data in shared preference

fun Context.booleanSharedPrefs(settingValue: SettingValue, defaultValue: Boolean = true) = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(settingValue.key, defaultValue)

fun Context.longSharedPrefs(settingValue: SettingValue, defaultValue: Long?) = PreferenceManager.getDefaultSharedPreferences(this).getLong(settingValue.key, defaultValue ?: 0)

fun Context.stringSharedPrefs(settingValue: SettingValue, defaultValue: String? = null) = PreferenceManager.getDefaultSharedPreferences(this).getString(settingValue.key, defaultValue)

// Retrofit call
fun <T> Call<List<T>>.getAll(): List<T> = this.execute().run {
    if (isSuccessful && !body().isNullOrEmpty()) {
        return body() as List<T>
    }
    throw RuntimeException("Response is empty or invalid")
}
fun <T> Call<T>.get(): T = this.execute().run {
    if (isSuccessful && body() != null) {
        return body() as T
    }
    throw RuntimeException("Response is empty or invalid")
}