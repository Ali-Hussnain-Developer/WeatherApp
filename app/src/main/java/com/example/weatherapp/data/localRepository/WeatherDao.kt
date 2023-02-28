package com.example.weatherapp.data.localRepository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.roomModel.WeatherModelClass

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(weather: WeatherModelClass)

    @Query("SELECT *FROM weatherTable")
    fun getAllData(): LiveData<List<WeatherModelClass>>
}
