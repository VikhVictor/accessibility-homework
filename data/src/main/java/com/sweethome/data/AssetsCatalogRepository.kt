package com.sweethome.data

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sweethome.domain.model.Category
import com.sweethome.domain.model.Product
import com.sweethome.domain.repository.CatalogRepository
import java.io.InputStreamReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetsCatalogRepository(
    private val assetManager: AssetManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CatalogRepository {

    private val gson = Gson()
    private val products: List<Product> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        readProducts()
    }
    private val categories: List<Category> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        readCategories()
    }

    override suspend fun loadCatalog(): List<Product> = withContext(ioDispatcher) {
        products
    }

    override suspend fun loadCategories(): List<Category> = withContext(ioDispatcher) {
        categories
    }

    private fun readProducts(): List<Product> =
        assetManager.open(PRODUCTS_FILE).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                gson.fromJson(
                    reader,
                    object : TypeToken<List<Product>>() {}.type
                )
            }
        }

    private fun readCategories(): List<Category> =
        assetManager.open(CATEGORIES_FILE).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                gson.fromJson(
                    reader,
                    object : TypeToken<List<Category>>() {}.type
                )
            }
        }

    private companion object {
        const val PRODUCTS_FILE = "products.json"
        const val CATEGORIES_FILE = "categories.json"
    }
}
