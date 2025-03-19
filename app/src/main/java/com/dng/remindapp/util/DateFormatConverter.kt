package com.dng.remindapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Long.convertToFullDateFormat(): String {
    val dateFormat = SimpleDateFormat("EEE, MMMM dd yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.toStringFormat(): String {
    val dateFormat = SimpleDateFormat("EEE, MMMM dd yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun String.toDateFormat(): Date? {
    val dateFormat = SimpleDateFormat("EEE, MMMM dd yyyy", Locale.getDefault())
    return dateFormat.parse(this)
}

fun Long.convertMillisToDateFormat(): String {
    val formatter = SimpleDateFormat("EEE, MMMM dd yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.convertDateStringToStartOfDay(): Long {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMMM dd yyyy")
    return LocalDate.parse(this, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()
        .toEpochMilli()
}
