package com.sweethome.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.sweethome.compose.R

@Composable
fun ProductImage(
    imageName: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
            .takeIf { it != 0 }
            ?: R.drawable.placeholder
    }
    Image(
        painter = painterResource(imageId),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
