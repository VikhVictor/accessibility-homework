package com.sweethome.compose.ui.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweethome.compose.R
import com.sweethome.compose.logic.CatalogSection
import com.sweethome.compose.ui.components.LegacyBlue
import com.sweethome.compose.ui.components.LegacyToolbar
import com.sweethome.compose.ui.components.ProductImage
import com.sweethome.item.FullItemViewModel

@Composable
fun CatalogScreen(
    sections: List<CatalogSection>,
    cartItemsCount: Int,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (FullItemViewModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LegacyToolbar(
            title = stringResource(R.string.shop_catalog_title),
            cartItemsCount = cartItemsCount,
            showCart = true,
            onBack = onBack,
            onCartClick = onCartClick
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sections, key = { it.title }) { section ->
                CatalogSection(
                    section = section,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

@Composable
private fun CatalogSection(
    section: CatalogSection,
    onProductClick: (FullItemViewModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = section.title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .height(56.dp)
                .padding(start = 16.dp, top = 16.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(section.products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: FullItemViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(168.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.size(164.dp)) {
            ProductImage(
                imageName = product.image,
                modifier = Modifier.fillMaxSize()
            )
            if (product.discount) {
                Image(
                    painter = painterResource(R.drawable.ic_discount),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(24.dp)
                )
            }
        }
        Text(
            text = product.collection,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = product.designer,
                color = LegacyBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(text = product.currency + product.price)
        }
    }
}
