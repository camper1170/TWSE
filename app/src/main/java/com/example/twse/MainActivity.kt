package com.example.twse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.twse.ui.theme.TWSETheme
import com.example.twse.view.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TWSETheme {
                MainScreen()
            }
        }
    }
}