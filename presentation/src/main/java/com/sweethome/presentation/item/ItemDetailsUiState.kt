package com.sweethome.presentation.item

import com.sweethome.domain.model.Product

data class ItemDetailsUiState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val cartItemsCount: Int = 0,
    val error: String? = null
)
