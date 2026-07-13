package com.sweethome.data

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sweethome.item.FullItemViewModel
import com.sweethome.shop.catalog.CategoryItem
import java.io.InputStreamReader

class AssetsCatalogRepository(
    private val assetManager: AssetManager
) : CatalogRepository {

    private val gson = Gson()

    override fun loadCatalog(): ArrayList<FullItemViewModel> =
        assetManager.open(PRODUCTS_FILE).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                gson.fromJson(
                    reader,
                    object : TypeToken<ArrayList<FullItemViewModel>>() {}.type
                )
            }
        }

    override fun loadCategories(): List<CategoryItem> =
        assetManager.open(CATEGORIES_FILE).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                gson.fromJson(
                    reader,
                    object : TypeToken<List<CategoryItem>>() {}.type
                )
            }
        }

    private companion object {
        const val PRODUCTS_FILE = "products.json"
        const val CATEGORIES_FILE = "categories.json"
    }
}
