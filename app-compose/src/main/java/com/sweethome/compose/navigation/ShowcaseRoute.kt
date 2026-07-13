package com.sweethome.compose.navigation

sealed interface ShowcaseRoute {
    data object Catalog : ShowcaseRoute

    data class ItemDetails(
        val itemId: String,
        val collection: String
    ) : ShowcaseRoute

    data object Cart : ShowcaseRoute

    data class Checkout(val fullPrice: String) : ShowcaseRoute
}
