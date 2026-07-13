package com.sweethome.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sweethome.compose.logic.ShowcaseLogic
import com.sweethome.compose.ui.SweetHomeComposeApp
import com.sweethome.compose.ui.theme.SweetHomeComposeTheme

class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = application as ComposeApplication
        val logic = ShowcaseLogic(
            application.catalogRepository,
            application.cartRepository
        )
        setContent {
            SweetHomeComposeTheme {
                SweetHomeComposeApp(
                    logic = logic,
                    onExit = ::finish
                )
            }
        }
    }
}
