package com.sweethome.presentation.catalog

import com.sweethome.domain.model.Product

data class CatalogUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryUiModel> = emptyList(),
    val cartItemsCount: Int = 0,
    val error: String? = null
)

data class CategoryUiModel(
    val title: String,
    val items: List<Product>
)
