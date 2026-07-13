package com.sweethome.compose.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sweethome.compose.logic.ShowcaseLogic
import com.sweethome.compose.navigation.ShowcaseRoute
import com.sweethome.compose.ui.cart.CartScreen
import com.sweethome.compose.ui.catalog.CatalogScreen
import com.sweethome.compose.ui.checkout.CheckoutScreen
import com.sweethome.compose.ui.components.LegacyTouchTargets
import com.sweethome.compose.ui.item.ItemDetailsScreen

@Composable
fun SweetHomeComposeApp(
    logic: ShowcaseLogic,
    onExit: () -> Unit
) {
    val backStack = remember { mutableStateListOf<ShowcaseRoute>(ShowcaseRoute.Catalog) }
    var cartRevision by remember { mutableIntStateOf(0) }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        } else {
            onExit()
        }
    }

    BackHandler(onBack = ::goBack)

    val cartItemsCount = cartRevision.let { logic.cartItemsCount() }
    LegacyTouchTargets {
        when (val route = backStack.last()) {
        ShowcaseRoute.Catalog -> CatalogScreen(
            sections = remember { logic.catalogSections() },
            cartItemsCount = cartItemsCount,
            onBack = ::goBack,
            onCartClick = { backStack.add(ShowcaseRoute.Cart) },
            onProductClick = { product ->
                backStack.add(
                    ShowcaseRoute.ItemDetails(
                        itemId = product.id,
                        collection = product.collection
                    )
                )
            }
        )

        is ShowcaseRoute.ItemDetails -> logic.product(route.itemId)?.let { product ->
            ItemDetailsScreen(
                product = product,
                cartItemsCount = cartItemsCount,
                onBack = ::goBack,
                onCartClick = { backStack.add(ShowcaseRoute.Cart) },
                onAddToCart = {
                    logic.addToCart(route.itemId)
                    cartRevision++
                }
            )
        }

        ShowcaseRoute.Cart -> CartScreen(
            snapshot = cartRevision.let { logic.cartSnapshot() },
            onBack = ::goBack,
            onCheckout = { fullPrice ->
                backStack.add(ShowcaseRoute.Checkout(fullPrice))
            }
        )

            is ShowcaseRoute.Checkout -> CheckoutScreen(
                fullPrice = route.fullPrice,
                onBack = ::goBack
            )
        }
    }
}
