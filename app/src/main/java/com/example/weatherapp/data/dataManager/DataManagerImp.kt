package com.example.weatherapp.data.dataManager

import androidx.lifecycle.LiveData
import com.example.weatherapp.model.roomModel.WeatherModelClass

interface DataManagerImp {
    suspend fun insertRecord(weather: WeatherModelClass)
    fun getAllData(): LiveData<List<WeatherModelClass>>
}
