package com.sweethome.compose.logic

import com.sweethome.data.CartRepository
import com.sweethome.data.CatalogRepository
import com.sweethome.item.FullItemViewModel

class ShowcaseLogic(
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) {
    fun catalogSections(): List<CatalogSection> {
        val productsByCategory = catalogRepository.loadCatalog().groupBy { it.category }
        val categories = catalogRepository.loadCategories()
        return productsByCategory.map { (categoryId, products) ->
            CatalogSection(
                title = categories.find { it.id == categoryId }?.name.orEmpty(),
                products = products
            )
        }
    }

    fun product(itemId: String): FullItemViewModel? =
        catalogRepository.loadCatalog().find { it.id == itemId }

    fun addToCart(itemId: String) {
        cartRepository.addItem(itemId)
    }

    fun cartItemsCount(): Int = cartRepository.itemsCount()

    fun cartSnapshot(): CartSnapshot {
        val products = catalogRepository.loadCatalog().filter {
            cartRepository.cartItems.keys.contains(it.id)
        }
        val fullPrice = products.sumOf { it.price.toInt() }
        return CartSnapshot(
            products = products,
            shipment = "₽255",
            itemsCount = products.sumOf { cartRepository.cartItems[it.id] ?: 0 },
            fullPrice = fullPrice.toString()
        )
    }
}

data class CatalogSection(
    val title: String,
    val products: List<FullItemViewModel>
)

data class CartSnapshot(
    val products: List<FullItemViewModel>,
    val shipment: String,
    val itemsCount: Int,
    val fullPrice: String
)
