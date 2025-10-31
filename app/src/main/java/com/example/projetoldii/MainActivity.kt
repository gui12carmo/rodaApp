package com.example.projetoldii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.example.projetoldii.data.AppDatabase
import com.example.projetoldii.ui.all.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(this)

        setContent {
            MaterialTheme {
                Surface {
                    MainScreen(db)
                }
            }
        }
    }
}
