package com.example.meter.extensions

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isNotEmail(): Boolean {
    return !(android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches())
}

fun String.toFormattedDate(): String {
    val locale = Locale("ka", "GE")
    val dateFormatSymbols = DateFormatSymbols(locale)
    dateFormatSymbols.weekdays = arrayOf(
        "Unused",
        "კვირა",
        "ორშაბათი",
        "სამშაბათი",
        "ოთხშაბათი",
        "ხუთშაბათი",
        "პარასკევი",
        "შაბათი"
    )

    val pattern = "dd MMMM"
    val formattedTime = SimpleDateFormat(pattern, dateFormatSymbols)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ka", "GE"))
    return formattedTime.format(sdf.parse(this)!!)
}
