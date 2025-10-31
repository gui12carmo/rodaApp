package com.example.projetoldii

import android.content.Context
import androidx.room.Room
import com.example.projetoldii.data.AppDatabase

object DatabaseProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "todo_database"
            )
                .fallbackToDestructiveMigration() // 🔥 Apaga e recria automaticamente se houver mudança
                .build()
        }
        return db!!
    }
}

