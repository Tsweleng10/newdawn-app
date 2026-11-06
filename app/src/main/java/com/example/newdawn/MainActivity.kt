package com.example.newdawn

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.newdawn.ui.navigation.AppNavigation
import com.example.newdawn.ui.theme.NewDawnBackground
import com.example.newdawn.ui.theme.NewDawnTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewDawnTheme {
                NewDawnBackground {
                    AppNavigation()
                }
            }
        }
    }
}