package com.example.weatherapp.data.localRepository

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.constants.Constants
import com.example.weatherapp.contextClass.ContextClass
import com.example.weatherapp.model.roomModel.WeatherModelClass

@Database(entities = [WeatherModelClass::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ContextClass.getContext(),
                    AppDatabase::class.java,
                    Constants.ROOM_DB_NAME,
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
