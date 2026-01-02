package com.mobix.differences


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mobix.differences.ui.navigation.AppNavHost
import com.mobix.differences.ui.theme.SpotDiffTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotDiffTheme {
                AppNavHost()
            }
        }
    }
}
