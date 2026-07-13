package com.sweethome.compose.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.DpSize

@Composable
fun LegacyTouchTargets(content: @Composable () -> Unit) {
    val viewConfiguration = LocalViewConfiguration.current
    val legacyConfiguration = object : ViewConfiguration by viewConfiguration {
        override val minimumTouchTargetSize: DpSize = DpSize.Zero
    }

    // Compose otherwise expands the deliberately undersized 20dp legacy controls to 48dp.
    CompositionLocalProvider(
        LocalViewConfiguration provides legacyConfiguration,
        content = content
    )
}
