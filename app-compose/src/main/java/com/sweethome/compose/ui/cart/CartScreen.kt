package com.sweethome.compose.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweethome.compose.R
import com.sweethome.compose.logic.CartSnapshot
import com.sweethome.compose.ui.components.LegacyActionText
import com.sweethome.compose.ui.components.LegacyBlack60
import com.sweethome.compose.ui.components.LegacyToolbar
import com.sweethome.compose.ui.components.ProductImage
import com.sweethome.item.FullItemViewModel

@Composable
fun CartScreen(
    snapshot: CartSnapshot,
    onBack: () -> Unit,
    onCheckout: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LegacyToolbar(
            title = stringResource(R.string.cart_title),
            cartItemsCount = 0,
            showCart = false,
            onBack = onBack,
            onCartClick = {}
        )
        Box(modifier = Modifier.weight(1f)) {
            if (snapshot.products.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.empty_cart),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(snapshot.products, key = { it.id }) { product ->
                        CartItem(product)
                        HorizontalDivider()
                    }
                }
            }
        }
        CartFooter(snapshot = snapshot, onCheckout = onCheckout)
    }
}

@Composable
private fun CartItem(product: FullItemViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        ProductImage(
            imageName = product.image,
            modifier = Modifier.size(60.dp)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = product.model, fontSize = 16.sp, maxLines = 2)
            Text(text = product.manufacturer.orEmpty(), fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(product.color), androidx.compose.foundation.shape.CircleShape)
                )
                Text(
                    text = product.colorName.orEmpty(),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CartFooter(
    snapshot: CartSnapshot,
    onCheckout: (String) -> Unit
) {
    val empty = snapshot.products.isEmpty()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        if (!empty) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = stringResource(R.string.delivery_from, snapshot.shipment))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.items_on, snapshot.itemsCount),
                        color = LegacyBlack60,
                        fontSize = 12.sp
                    )
                    Text(text = "₽${snapshot.fullPrice}", fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        LegacyActionText(
            text = stringResource(R.string.confirm_checkout),
            enabled = !empty,
            onClick = { onCheckout(snapshot.fullPrice) }
        )
    }
}
