package com.example.rewardapp.database

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconKeys {
    const val CAKE = "CAKE"
    const val BATH_THUB = "CAKE"
    const val TV = "TV"
}
val rewardIcons = mapOf<String, ImageVector>(
    Pair(IconKeys.CAKE, Icons.Default.Create),
    Pair(IconKeys.BATH_THUB, Icons.Default.Email),
    Pair(IconKeys.TV, Icons.Default.Face)

)

enum class IconKeysEn(val reward: ImageVector) {
    CAKE(Icons.Default.Notifications),
    BATH_THUB(Icons.Default.Face),
    TV(Icons.Default.AddCircle)
}

val defaulticon = IconKeysEn.CAKE
