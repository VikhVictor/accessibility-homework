package com.sweethome.compose

import android.app.Application
import com.sweethome.data.AssetsCatalogRepository
import com.sweethome.data.CartRepository
import com.sweethome.data.CatalogRepository
import com.sweethome.data.InMemoryCartRepository

class ComposeApplication : Application() {
    lateinit var catalogRepository: CatalogRepository
        private set

    lateinit var cartRepository: CartRepository
        private set

    override fun onCreate() {
        super.onCreate()
        catalogRepository = AssetsCatalogRepository(assets)
        cartRepository = InMemoryCartRepository()
    }
}
