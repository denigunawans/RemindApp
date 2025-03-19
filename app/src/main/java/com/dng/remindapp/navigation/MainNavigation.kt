package com.dng.remindapp.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dng.remindapp.R
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Wallet

@Serializable
object Todo

@Serializable
object NoteRoute

@Serializable
object AddRecord

@Serializable
object AddTodo

@Serializable
object AddNote

data class MainRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector
)


