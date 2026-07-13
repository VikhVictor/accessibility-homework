package com.sweethome

import android.app.Application
import com.sweethome.data.AssetsCatalogRepository
import com.sweethome.data.InMemoryCartRepository
import com.sweethome.domain.repository.CartRepository
import com.sweethome.domain.repository.CatalogRepository

class SweetHomeApplication : Application() {

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
