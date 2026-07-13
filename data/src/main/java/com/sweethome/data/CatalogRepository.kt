package com.sweethome.data

import com.sweethome.item.FullItemViewModel
import com.sweethome.shop.catalog.CategoryItem

interface CatalogRepository {
    fun loadCatalog(): ArrayList<FullItemViewModel>

    fun loadCategories(): List<CategoryItem>
}
