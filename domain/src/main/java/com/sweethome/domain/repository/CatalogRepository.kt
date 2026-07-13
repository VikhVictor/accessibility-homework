package com.sweethome.domain.repository

import com.sweethome.domain.model.Category
import com.sweethome.domain.model.Product

interface CatalogRepository {
    suspend fun loadCatalog(): List<Product>

    suspend fun loadCategories(): List<Category>
}
