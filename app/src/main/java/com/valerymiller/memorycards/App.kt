package com.valerymiller.memorycards

import android.app.Application
import androidx.room.Room
import com.valerymiller.memorycards.data.room.AppDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "database"
        ).build()
    }

    fun getDatabase(): AppDatabase {
        return database
    }
}