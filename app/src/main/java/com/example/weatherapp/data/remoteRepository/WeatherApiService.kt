package com.example.weatherapp.data.remoteRepository

import com.example.weatherapp.model.apiModel.threeHourly.ThreeHourlyModel
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApiService {

    @POST("forecast")
    fun getHourlyWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
    ): Call<ThreeHourlyModel>
}
