package com.dng.remindapp.util

fun String.textInputValidation(): Boolean{
    return this.isNotEmpty() && this.isNotBlank()
}

fun Int.numberInputValidation(): Boolean{
    return this > 0
}