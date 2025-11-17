package com.example.projetoldii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.projetoldii.data.AppDatabase
import com.example.projetoldii.ui.all.MainScreen
import com.example.projetoldii.ui.all.ProjetoLDIITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(this)

        setContent {
            ProjetoLDIITheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(db)
                }
            }
        }
    }
}
