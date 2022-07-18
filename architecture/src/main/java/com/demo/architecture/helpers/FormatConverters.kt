package com.demo.architecture.helpers

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.demo.architecture.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Numbers formatting
 */
val doubleFormat = DecimalFormat().apply {
    maximumFractionDigits = 1
    isGroupingUsed = true
    groupingSize = 3
    decimalFormatSymbols = decimalFormatSymbols.apply {
        decimalSeparator = '.'
        groupingSeparator = ' '
    }
}
val intFormat = DecimalFormat().apply {
    isGroupingUsed = true
    groupingSize = 3
    decimalFormatSymbols = decimalFormatSymbols.apply { groupingSeparator = ' ' }
}

fun intToStrForDisplay(value: Int): String = intFormat.format(value)

fun doubleToStrForDisplay(value: Double): String = doubleFormat.format(value)

fun doubleToStr(value: Double): String = "%.1f".format(Locale.US, value)


/**
 * Date formatting
 */
fun dateToStrForDisplay(date: Long): String {
    val form = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return form.format(Date(date))
}

fun dayOfWeekToResForDisplay(date: Long): Int {
    val calendar = GregorianCalendar.getInstance().apply { timeInMillis = date }

    return when (calendar.get(GregorianCalendar.DAY_OF_WEEK)) {
        GregorianCalendar.MONDAY -> R.string.monday
        GregorianCalendar.TUESDAY -> R.string.tuesday
        GregorianCalendar.WEDNESDAY -> R.string.wednesday
        GregorianCalendar.THURSDAY -> R.string.thursday
        GregorianCalendar.FRIDAY -> R.string.friday
        GregorianCalendar.SATURDAY -> R.string.saturday
        else -> R.string.sunday
    }
}

fun monthToStrForDisplay(date: Long): String {
    val form = SimpleDateFormat("MM.yy", Locale.getDefault())
    return form.format(Date(date))
}

fun yearToStrForDisplay(date: Long): String {
    val form = SimpleDateFormat("yy", Locale.getDefault())
    return form.format(Date(date))
}


/**
 * Percents formatting
 */
fun percentsToStr(value: Int): String = "$value%"

fun percentsToStr(value: Double): String = "${value.toInt()}%"


/**
 * Classes external functions
 */
fun Fragment.isPermissionGranted(permission: String): Boolean {
    context?.let {
        return PermissionChecker.checkSelfPermission(
            it,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }
    return false
}

fun Uri.getOriginalFileName(): String? {
    return path!!.substringAfterLast('/', "")
}

fun View.setVisibility(isVisible: Boolean) {
    this.visibility =
        if (isVisible) View.VISIBLE
        else View.INVISIBLE
}

fun <T> MutableCollection<T>.addAllNotExisting(list: List<T>) {
    if (this.isEmpty()) this.addAll(list)
    else {
        for (i in list) {
            var add = true
            for (c in this) {
                if (i == c) {
                    add = false
                    return
                }
            }
            if (add) this.add(i)
        }
    }
}

//inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
