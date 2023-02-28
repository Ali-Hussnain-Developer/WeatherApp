package com.example.weatherapp.data.dataManager

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.localRepository.AppDatabase
import com.example.weatherapp.model.roomModel.WeatherModelClass

object DataManager : DataManagerImp {
    override suspend fun insertRecord(weather: WeatherModelClass) {
        AppDatabase.getDatabase().weatherDao().insertRecord(weather)
    }

    override fun getAllData(): LiveData<List<WeatherModelClass>> {
        val list: LiveData<List<WeatherModelClass>>
        list = AppDatabase.getDatabase().weatherDao().getAllData()
        return list
    }

}
