package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.dataManager.DataManager
import com.example.weatherapp.model.roomModel.WeatherModelClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel() : ViewModel() {
    var dataManager = DataManager
    var allweatherData: LiveData<List<WeatherModelClass>>

    init {
        allweatherData = dataManager.getAllData()
    }

    fun insertRecord(weather: WeatherModelClass) = viewModelScope.launch(Dispatchers.IO) {
        dataManager.insertRecord(weather)
    }
}
