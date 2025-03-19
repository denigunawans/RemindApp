package com.dng.remindapp.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun showSnackbar(scope: CoroutineScope, snackbarHostState: SnackbarHostState,message: String){
    scope.launch(Dispatchers.Main) {
        snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        println("SNACKBAR")
    }
}