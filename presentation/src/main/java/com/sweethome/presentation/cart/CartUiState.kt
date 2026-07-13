package com.sweethome.presentation.cart

import com.sweethome.domain.model.Product

data class CartUiState(
    val isLoading: Boolean = true,
    val items: List<Product> = emptyList(),
    val shipment: String = "₽255",
    val itemsCount: Int = 0,
    val totalPrice: String = "₽0",
    val checkoutPrice: String = "0",
    val error: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && items.isEmpty()
}
