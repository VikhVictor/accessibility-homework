package com.sweethome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sweethome.data.AssetsCatalogRepository
import com.sweethome.data.InMemoryCartRepository

class MainActivity : AppCompatActivity() {

    private lateinit var rootRouter: RootRouterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootRouter = RootRouterImpl(
            supportFragmentManager,
            InMemoryCartRepository(),
            AssetsCatalogRepository(assets)
        )
        rootRouter.showCatalog()
    }

    override fun onBackPressed() {
        if (rootRouter.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
