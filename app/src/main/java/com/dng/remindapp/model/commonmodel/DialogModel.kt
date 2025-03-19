package com.dng.remindapp.model.commonmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class DialogModel(
    var title: String = "Title",
    var description: String = "Description",
    var icon: ImageVector = Icons.Outlined.Info,
    var onConfirm: () -> Unit = {},
    var onDismiss: () -> Unit = {}
)
