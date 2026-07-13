package com.sweethome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var rootRouter: RootRouterImpl
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootRouter = RootRouterImpl(supportFragmentManager)
        if (savedInstanceState == null) {
            rootRouter.showCatalog()
        }
    }

    override fun onBackPressed() {
        if (rootRouter.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
