package com.dng.remindapp.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

fun showSnackbar(scope: CoroutineScope, snackbarHostState: SnackbarHostState,message: String){
    scope.launch(Dispatchers.Main) {
        snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        println("SNACKBAR")
    }
}

fun numberToCurrencyFormat(number: Double): String{
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.maximumFractionDigits = 2
    formatter.currency = Currency.getInstance("IDR")
    println(number)
    println(formatter.format(number))

    return formatter.format(number)
}

fun numberToSimpleMoneyFormat(number: Double): String{
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}