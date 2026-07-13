package com.sweethome.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweethome.compose.R

@Composable
fun LegacyToolbar(
    title: String,
    cartItemsCount: Int,
    showCart: Boolean,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(12.dp)
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(20.dp)
                .clickable(onClick = onBack)
        )

        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 72.dp, end = if (showCart) 72.dp else 16.dp)
        )

        if (showCart) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(56.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_shopping_cart),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(20.dp)
                        .clickable(onClick = onCartClick)
                )
                if (cartItemsCount > 0) {
                    Text(
                        text = cartItemsCount.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(16.dp)
                            .background(LegacyBlue, shape = androidx.compose.foundation.shape.CircleShape)
                            .padding(top = 1.dp, start = 4.dp)
                    )
                }
            }
        }
    }
}
