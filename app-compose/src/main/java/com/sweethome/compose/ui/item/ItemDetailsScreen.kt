package com.sweethome.compose.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweethome.compose.R
import com.sweethome.compose.ui.components.LegacyActionText
import com.sweethome.compose.ui.components.LegacyBlack12
import com.sweethome.compose.ui.components.LegacyBlack55
import com.sweethome.compose.ui.components.LegacyToolbar
import com.sweethome.compose.ui.components.ProductImage
import com.sweethome.item.FullItemViewModel

@Composable
fun ItemDetailsScreen(
    product: FullItemViewModel,
    cartItemsCount: Int,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LegacyToolbar(
            title = product.collection,
            cartItemsCount = cartItemsCount,
            showCart = true,
            onBack = onBack,
            onCartClick = onCartClick
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            ProductImage(
                imageName = product.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = product.model,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            Text(
                text = "${product.currency} ${product.price}",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 32.dp)
            )
            LegacyActionText(
                text = stringResource(R.string.add_to_cart),
                onClick = onAddToCart,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            HorizontalDivider(color = LegacyBlack12)
            Text(
                text = stringResource(R.string.about_title),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
            )
            Text(
                text = product.about,
                color = LegacyBlack55,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
            )
            HorizontalDivider(color = LegacyBlack12)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.designer_field), fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = product.designer, fontSize = 16.sp)
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_forward),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}
